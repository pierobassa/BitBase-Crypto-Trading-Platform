package application;

import java.io.IOException;

import application.controller.DepositAmountDialogController;
import application.controller.DepositDialogController;
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

public class DepositDialog {
	private static DepositDialog instance = null;
	private static Stage window;
	private Scene sceneDialog;
	private DepositDialogController cardController;
	private DepositAmountDialogController amountController;
	
	private Parent cardRoot;
	private Parent amountRoot;
	private FXMLLoader cardLoader;
	private FXMLLoader amountLoader;
	
	private Double depositAmount;
	
	DepositDialog() throws IOException{		
		window = new Stage();
		Image image = new Image(getClass().getResourceAsStream("/images/icons/stage-icon.png"));
		window.getIcons().add(image);
		amountLoader = new FXMLLoader(getClass().getResource("/application/view/DepositAmountDialog.fxml"));
		amountRoot = (Parent) amountLoader.load();
		sceneDialog = new Scene(amountRoot);
		sceneDialog.setFill(Color.TRANSPARENT);
		window.setScene(sceneDialog);
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		window.initStyle(StageStyle.TRANSPARENT);
		
		window.setOnCloseRequest(new EventHandler<WindowEvent>() { //If the user closes the Stage forcefully (not using the close button on the UI) then the main Scene needs to remove the blur effect and reset to the first scene of the DepositDialog.
			
			@Override
			public void handle(WindowEvent event) {	
				SceneHandler.getInstance().removeSceneBlur();		
				try {
					DepositDialog.getInstance().goBack();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		amountController = amountLoader.getController();

		cardLoader = new FXMLLoader(getClass().getResource("/application/view/DepositDialog.fxml"));
		cardRoot = (Parent) cardLoader.load();
		cardController = cardLoader.getController();
	}
	
	public static DepositDialog getInstance() throws IOException {
		if(instance == null)
			instance = new DepositDialog();
		
		return instance ;
	}
	
	public void open() {
		window.show();
		SceneHandler.getInstance().setSceneBlur();
	}
	
	public void goToPayment(Double deposit) throws IOException {		
		sceneDialog.setRoot(cardRoot);
		window.setScene(sceneDialog);
		
		depositAmount = deposit;
		
		window.show();
	}
	
	public void goBack() throws IOException {		
		sceneDialog.setRoot(amountRoot);
		window.setScene(sceneDialog);
		
		window.show();
	}
	
	public Double getDepositAmount() {
		return depositAmount;
	}

	public void updateBalance() {
		if(amountController != null)
			amountController.updateBalance();
	}
	
	public void resetCard() {
		cardController.setInitValues();
	}
	
	public static void close() {
		if(window != null)
			window.close();
	}
}
