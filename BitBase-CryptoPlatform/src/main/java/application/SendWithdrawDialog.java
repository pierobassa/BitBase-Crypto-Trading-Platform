package application;

import java.io.IOException;
import application.controller.SendController;
import application.controller.WithdrawController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class SendWithdrawDialog {
	
	private static SendWithdrawDialog instance = null;
	private static Stage window;
	private Scene sceneDialog;
	private SendController sendController;
	private WithdrawController withdrawController;
	
	private Parent sendRoot;
	private Parent withdrawRoot;
	private Parent sendWithdrawRoot;
	private FXMLLoader sendLoader;
	private FXMLLoader withdrawLoader;
	private FXMLLoader sendWithdrawLoader;	
	

	public SendWithdrawDialog() throws IOException {
		window = new Stage();
		Image image = new Image(getClass().getResourceAsStream("/images/icons/stage-icon.png"));
		window.getIcons().add(image);
		sendWithdrawLoader = new FXMLLoader(getClass().getResource("/application/view/SendWithdraw.fxml"));
		sendWithdrawRoot = (Parent) sendWithdrawLoader.load();
		sceneDialog = new Scene(sendWithdrawRoot);
		sceneDialog.setFill(Color.TRANSPARENT);
		window.setScene(sceneDialog);
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		window.initStyle(StageStyle.TRANSPARENT);
	
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {	
				SceneHandler.getInstance().removeSceneBlur();		
				try {
					SendWithdrawDialog.getInstance().goBack();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		sendLoader = new FXMLLoader(getClass().getResource("/application/view/Send.fxml"));
		sendRoot = (Parent) sendLoader.load();
		sendController = sendLoader.getController();
		
		withdrawLoader = new FXMLLoader(getClass().getResource("/application/view/Withdraw.fxml"));
		withdrawRoot = (Parent) withdrawLoader.load();
		withdrawController = withdrawLoader.getController();
	}
	
	public static SendWithdrawDialog getInstance() throws IOException {
		if(instance == null)
			instance = new SendWithdrawDialog();
		
		return instance ;
	}
	
	public void open() {
		window.show();
		SceneHandler.getInstance().setSceneBlur();
	}
	
	public void goToSend() throws IOException {				
		sceneDialog.setRoot(sendRoot);
		window.setScene(sceneDialog);
		
		window.show();
	}
	

	public void goToWithdraw() throws IOException {				
		sceneDialog.setRoot(withdrawRoot);
		window.setScene(sceneDialog);
		
		withdrawController.updateBalance();
		
		window.show();
	}
	
	public void goBack() throws IOException {	
		resetInfo();
		sceneDialog.setRoot(sendWithdrawRoot);
		window.setScene(sceneDialog);
		
		window.show();
	}
	
	public void updateBalance() {
		withdrawController.updateBalance();
	}
	
	public void resetInfo() {
		withdrawController.resetInfo();
		sendController.resetInfo();
	}
	
	public static void close() {
		if(window != null)
			window.close();
	}
}
