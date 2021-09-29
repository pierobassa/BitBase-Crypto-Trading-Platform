package application.data.tasks;

import java.io.IOException;
import java.util.Vector;

import application.ClientView;
import application.MsgDialog;
import application.net.common.API;
import application.data.DataServiceArticles;
import application.model.ClientLogic;
import application.model.News;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class UpdateNews extends Task<Void>{

	private static DataServiceArticles dataServiceArticles;
	private static Timeline timeline;
	
	public UpdateNews() {
		dataServiceArticles = new DataServiceArticles();
	}
	
	@Override
	protected Void call() throws Exception {
		getNews();
		updateNews();
		
		return null;
	}
	
	public static void stop() {
		timeline.stop();
		dataServiceArticles.cancel();
	}
	
	private void getNews() {
		 dataServiceArticles.setDati(API.getInstance().getNewsAPI());
	     dataServiceArticles.restart();
	     dataServiceArticles.setOnSucceeded(new EventHandler<WorkerStateEvent>() {	    	
			
	    	@Override
			public void handle(WorkerStateEvent event) {
	    		
				@SuppressWarnings("unchecked")
				Vector<News> news = (Vector<News>) event.getSource().getValue();
				if(news == null) {
					stop();
					try {
						MsgDialog.getInstance().showError("Connection lost to the internet. Please login again");
						ClientLogic.getInstance().resetClientLogic();
					} catch (IOException e) {
						e.printStackTrace();
					}  
					return;
				}	
				ClientView.getInstance().setNewsHome(news); //Updates the news in the home screen
				ClientLogic.getInstance().setNewsReady(true);
				ClientView.getInstance().playNewsTransition();
	    	}
		});		
	}

	private void updateNews() {
		timeline =  new Timeline(new KeyFrame(Duration.minutes(15), new EventHandler<ActionEvent>() { //Retrieves the news every 15 minutes.

			@Override
			public void handle(ActionEvent event) {
				getNews();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
}
