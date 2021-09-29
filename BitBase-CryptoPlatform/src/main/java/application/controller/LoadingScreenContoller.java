package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.MsgDialog;
import application.SceneHandler;
import application.model.ClientLogic;
import application.net.client.ClientService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class LoadingScreenContoller implements Initializable{
	
	@FXML
	private Pane pane;
	
	@FXML
	private Pane logoutPane;

	@FXML
	private ImageView loadingImg;

	@FXML
	private Label baseLbl;

	@FXML
	private Label changingLbl;
	    
	private String[] loads = {"Dashboard", "Experience", "Trader", "Friend", "Platform", "Environment"};
	
	private Timeline labelChanger;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 Image image = new Image(getClass().getResourceAsStream("/images/loadingScreen.png"));
	     loadingImg.setImage(image);
		
		 changingLbl.setStyle("-fx-text-fill:  #ff69eb");
			labelChanger = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() { //Changes the label on the loading screen, scrolling between the Strings in 'loads'
				private int i = 0;
				private int timer = 0;
	    	 	@Override
	    	 	public void handle(ActionEvent event) {

	    	 		if(i % 2 == 0) 
	    				changingLbl.setStyle("-fx-text-fill:  #46ffff;");
	    			else
	    				changingLbl.setStyle("-fx-text-fill:  #ff69eb;");
	    	 		
	    	 		if(i>3 && ClientLogic.getInstance().allReady()) { //If everything is ready then the home page can appear
	    	 			stop();
	    	 			
	    	 			try {
	    	 				MsgDialog.getInstance().close();
	    	 				SceneHandler.getInstance().setHomeScene();
						} catch (Exception e) {
							
							e.printStackTrace();
						}
	    	 		}
	    	 		if(i>5)
	    	 			i = 0;
	    	 		changingLbl.setText(loads[i]);
	    	 		i++;
	    	 		
	    	 		if(timer == 30) {
						try {
							if(!ClientLogic.getInstance().statsReady()) {
								MsgDialog.getInstance().showInfo(4);
							}
						} catch (IOException e) {
							
							e.printStackTrace();
						}
	    	 		}
	    	 		
	    	 		if(timer == 60) {
	    	 			try {
							MsgDialog.getInstance().showError("Connection timed out. Please try again.");
							ClientLogic.getInstance().resetClientLogic();
		    	 			ClientService.getInstance().resetClient();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
	    	 			
	    				SceneHandler.getInstance().setLoginScene();
	    	 		}
	    	 		
	    	 		timer++;
	    	 	}	    	 	
	     }));
			labelChanger.setCycleCount(Timeline.INDEFINITE);
			labelChanger.play();
	}

	public void stop() {
		labelChanger.stop();
	}
}
