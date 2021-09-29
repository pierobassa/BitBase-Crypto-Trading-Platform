package application.data.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import application.ClientView;
import application.MsgDialog;
import application.net.common.API;
import application.data.DataServiceLast30Days;
import application.model.ClientLogic;
import application.model.PriceDate;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class Update30DayPrices extends Task<Void>{
	
	private static Map<String, DataServiceLast30Days> dataServices30Days;
	public Update30DayPrices() {
		 dataServices30Days = new HashMap<String, DataServiceLast30Days>();

		 for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			 if(key != Protocol.USD)
				 dataServices30Days.put(key, new DataServiceLast30Days());
		 }
	}

	@Override
	protected Void call() throws Exception {	
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			 if(key != Protocol.USD)
				 get30DayPrices(dataServices30Days.get(key), key);
		}
		
		return null;
	}
	
	public static void stop() {
		
		for(var key : dataServices30Days.keySet())
			dataServices30Days.get(key).cancel();
	}

	private void get30DayPrices(DataServiceLast30Days dataServiceLast30Days, String crypto) {	
		dataServiceLast30Days.setDati(API.getInstance().get30DayAPI(crypto));
		dataServiceLast30Days.start();
		dataServiceLast30Days.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
	    	@Override
			public void handle(WorkerStateEvent event) {
				@SuppressWarnings("unchecked")
				
				Vector<PriceDate> prices = (Vector<PriceDate>) event.getSource().getValue();
				if(prices == null) {
					try {
						stop();
						MsgDialog.getInstance().showError("Connection lost to the internet. Please login again");
						ClientLogic.getInstance().resetClientLogic();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					return;
				}

				Prices.getInstance().addPrices30Days(crypto,  new Vector<PriceDate>(prices)); //Sets the last 30 day prices for the specific asset
	
				ClientView.getInstance().initCrypto(crypto, prices); //Initializes the home screen charts 
				ClientLogic.getInstance().set30DayPricesReady(crypto, true); //Sets the 30day asset flag true
	    	}
		});
	}
}
