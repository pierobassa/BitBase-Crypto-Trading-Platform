package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.SceneHandler;
import application.SendWithdrawDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SendWithdrawController implements Initializable{

    @FXML
    private Pane sendBtn;

    @FXML
    private Pane withdrawBtn;

    @FXML
    private ImageView sendImg;

    @FXML
    private ImageView withdrawImg;

    @FXML
    private Pane closeBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initImages();
		
	}

	 private void initImages() {
		 Image image = new Image(getClass().getResourceAsStream("/images/send.png"));
	     sendImg.setImage(image);
	     image = new Image(getClass().getResourceAsStream("/images/withdraw.png"));
	     withdrawImg.setImage(image);
	}

	@FXML
	public void closeDialog(MouseEvent event) {
		closeBtn.getScene().getWindow().hide();
		SceneHandler.getInstance().removeSceneBlur();
	}	
	
	@FXML
	public void goToSend(MouseEvent event) throws IOException {
		SendWithdrawDialog.getInstance().goToSend();
	}	
	
	@FXML
	public void goToWithdraw(MouseEvent event) throws IOException {
		SendWithdrawDialog.getInstance().goToWithdraw();
	}	
}
