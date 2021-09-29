package application.net.server.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import application.net.common.Stats;
import application.net.server.Server;

public class ThreadCryptoStats implements Runnable{  //Thread that retieves the asset stats for each Supported Asset
	
	private String data;
	private String crypto;
	
	public void setDati(String data, String crypto) {
		this.data = data;
		this.crypto = crypto;
	}

	@Override
	public void run() {
		try {					
			String url = data;
			URL obj = new URL(url);
			
			URLConnection con = obj.openConnection();
			
			con.setConnectTimeout(5000);
			con.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			inputLine = in.readLine();
			while(inputLine != null) {
				response.append(inputLine);
				inputLine = in.readLine();
			}
			in.close();
			
			
			String responseStr = response.toString();
			responseStr = responseStr.substring(1, responseStr.length()-1);			
			
			JSONObject jsonResponse = new JSONObject(responseStr);
			
			Double market_cap = jsonResponse.getDouble("market_cap");
			Double volume_24h = jsonResponse.getDouble("total_volume");
			Double circulating_supply = jsonResponse.getDouble("circulating_supply");
			
			Stats stats = new Stats(market_cap, circulating_supply, volume_24h);
			Server.setStats(crypto, stats);
			Server.setCryptoStatsReady(crypto, true);
		} catch (Exception e) {
			System.out.println("[SERVER] ERROR WHILE FETCHING ASSET STATS. CHECK CONNECTION AND RESTART THE SERVER.");
			Server.setStatsReady(false);
		}
	}

}
