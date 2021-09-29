package application.data.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import application.ClientView;
import application.MsgDialog;
import application.net.common.API;
import application.data.DataServiceLastPrice;
import application.model.ClientLogic;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class UpdateCurrentPrices extends Task<Void> {
	
	private static Map<String, DataServiceLastPrice> lastPricesServices;
	private static Map<String, Timeline> timeline;
	
	public UpdateCurrentPrices() {
		lastPricesServices = new HashMap<String, DataServiceLastPrice>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				 lastPricesServices.put(key, new DataServiceLastPrice());
			}
		}
	    
	    timeline = new HashMap<String, Timeline>();
	    
	}
	
	 public static void stop() {
		 for(var key : timeline.keySet())
			 timeline.get(key).stop();
		 
		 for(var key : lastPricesServices.keySet())
			 lastPricesServices.get(key).cancel();
	 }
	

	protected Void call() throws Exception {    	 
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				getPrice(key);
			}
		}
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				livePrice(key);
			}
		}    
		return null;
	}
	
	private void getPrice(String crypto){
		lastPricesServices.get(crypto).setData(API.getInstance().getLivePriceAPI(crypto));
 		lastPricesServices.get(crypto).restart();
 		lastPricesServices.get(crypto).setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {     
					Double price = (Double) event.getSource().getValue();
					if(price == null) {
						stop();
						try {
							MsgDialog.getInstance().showError("Lost internet connection. Please login again"); //If there has been an interruption of Internet connection, then the app must be restarted because prices would not be updated and the user could buy an asset with an incorrect price
							ClientLogic.getInstance().resetClientLogic();
						} catch (IOException e) {
							e.printStackTrace();
						}  
						return;
					}
					
					Prices.getInstance().setCurrentPrice(crypto, price); //Sets the updated price for the asset
					
					ClientView.getInstance().setPrice(crypto, price); //Updates the price on the UI
					ClientLogic.getInstance().setPricesReady(crypto, true); //Sets the flag ready for the specific asset
					
				}               				
			});
	}
	
	private void livePrice(String crypto) {
		timeline.put(crypto, new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() { //Retrieves the price for each asset every 10 seconds

			@Override
			public void handle(ActionEvent event) {
				getPrice(crypto);
			}
		})));
		timeline.get(crypto).setCycleCount(Timeline.INDEFINITE);
		timeline.get(crypto).play();
	}
}
