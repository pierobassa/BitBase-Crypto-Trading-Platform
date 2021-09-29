package application.net.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Timer;

import application.net.common.Protocol;
import application.net.common.Stats;
import application.net.common.SupportedAssets;
import application.net.server.thread.UpdateCryptoStats;

public class Server implements Runnable {

	private ServerSocket server;
	private ExecutorService executor;
	private static Map<String, Boolean> statsReadyMap;
	private static Map<String, Stats> cryptoStats;
	private static Boolean statsReady = false;
	
	public void startServer() {
		try {
			server = new ServerSocket(8000);
			executor = Executors.newCachedThreadPool();
			Thread t = new Thread(this);
			t.start();
			
			Log.getInstance();
			
			statsReadyMap = new HashMap<String, Boolean>();
			cryptoStats = new HashMap<String, Stats>();
			for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
				if(key != Protocol.USD) {
					statsReadyMap.put(key, false);
				}
			}	
			
			updateStats();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("[SERVER] Waiting for connections...");
				Socket socket = server.accept();
				System.out.println("[SERVER] Client connected...");
				MessagesHandler m = new MessagesHandler(socket);
				executor.submit(m); //RUN m
			} catch (IOException e) {
				return;
			}
			
		}
	}
	
	private static void updateStats() { //Retrieves the asset statistics. The server performs this because the API provider has a limit of the number of requests per minute.
		System.out.println("[SERVER] Fetching crypto stats. Clients can't connect until completed.");
		UpdateCryptoStats updateCryptoStats = new UpdateCryptoStats();
    	updateCryptoStats.run();
	}
	
	public static void setStats(String crypto, Stats stats) {
		cryptoStats.put(crypto, stats);
	}

	public static void setCryptoStatsReady(String crypto, Boolean ready) throws InterruptedException, IOException {
		statsReadyMap.put(crypto, ready);
		
		if(!statsReady)
			System.out.println("[SERVER] " + crypto + " stats ready wait for all stats to complete");
		else
			System.out.println("[SERVER] " + crypto + " stats updated");
		
		for(var key : statsReadyMap.keySet())
			if(statsReadyMap.get(key) == false)
				return;
		
		if(!statsReady) {
			statsUpdater();
			System.out.println("[SERVER] ALL STATS READY");	
		}
		
		statsReady = true;
	}
	
	private static void statsUpdater() throws IOException {
		Timer timer = new Timer(60*30*1000, new ActionListener() { //60*30*1000 every 30 minutes updates the stats
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					updateStats();
					MessagesHandler.sendUpdatedStats();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		timer.setRepeats(true); 
		timer.start(); 
		
	}

	public synchronized static Boolean statsReady() {
		return statsReady;
	}
	
	public synchronized static Map<String, Stats> getCryptoStats(){
		return cryptoStats;
	}
	
	public static void setStatsReady(Boolean flag) {
		statsReady = flag;
	}
}
