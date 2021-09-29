package application;

import java.io.IOException;

import application.controller.MyAccountController;
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

public class MyAccountDialog {

	private static MyAccountDialog instance = null;
	
	private static Stage window;
	private Scene sceneDialog;
	private MyAccountController myAccountController;
	private Parent myAccountRoot;
	private FXMLLoader myAccountLoader;	
	
	MyAccountDialog() throws IOException {		
		window = new Stage();
		Image image = new Image(getClass().getResourceAsStream("/images/icons/stage-icon.png"));
		window.getIcons().add(image);
		myAccountLoader = new FXMLLoader(getClass().getResource("/application/view/MyAccount.fxml"));
		myAccountRoot = (Parent) myAccountLoader.load();
		sceneDialog = new Scene(myAccountRoot);
		sceneDialog.setFill(Color.TRANSPARENT);
		window.setScene(sceneDialog);
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		window.initStyle(StageStyle.TRANSPARENT);
		
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {	
				SceneHandler.getInstance().removeSceneBlur();				
			}
		});
		
		myAccountController = myAccountLoader.getController();
	}

	public static MyAccountDialog getInstance() throws IOException {
		if(instance == null)
			instance = new MyAccountDialog();
		
		return instance;
	}
	
	public void open() {
		window.show();
		SceneHandler.getInstance().setSceneBlur();
	}
	
	public void reset() {
		myAccountController.reset();
	}

	public static void close() {
		if(window != null)
			window.close();
	}
}
