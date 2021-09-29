package application;
import java.io.IOException;
import application.controller.DialogController;
import application.net.common.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MsgDialog {
	private Stage window;
	private Scene sceneDialog;
	private DialogController controller;
	
	private static MsgDialog instance = null;
	
	MsgDialog() throws IOException {
		window = new Stage();
		Image image = new Image(getClass().getResourceAsStream("/images/icons/stage-icon.png"));
		window.getIcons().add(image);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Dialog.fxml"));
		Parent root = (Parent) loader.load();		
		sceneDialog = new Scene(root);
		sceneDialog.setFill(Color.TRANSPARENT);
		window.setScene(sceneDialog);
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		window.initStyle(StageStyle.TRANSPARENT);
		
		controller = loader.getController();
	}
	
	public static MsgDialog getInstance() throws IOException {
		if(instance == null)
			instance = new MsgDialog();
		
		return instance;
	}
	
	public void showPendingDialog() {
		controller.setPendingDialog();		
		window.show();
	}
	
	public void showTransactionSuccessful() {
		controller.setTransactionSuccessfulDialog();
		window.show();
	}
	
	public void showWithdrawSuccessful() {
		controller.setWithdrawSuccessfulDialog();
		window.show();
	}
	
	public void showError(String text) { //Error event dialog
		controller.setError(text);
		window.show();
	}
	
	public void showSuccess(String text) { //Successful event dialog
		controller.setSuccess(text);
		window.show();
	}
	
	public void showInfo(int pos) { //Info event dialog
		controller.setShowInfo(pos);
		window.show();
	}
	
	public void close() {
		if(window.isShowing())
			window.close();
	}

	public void showReceived(Transaction transaction) { //Received asset dialog when user receives an asset from another user with the SEND feature.
		controller.setReceivedAsset(transaction);
		window.show();
	}
}
