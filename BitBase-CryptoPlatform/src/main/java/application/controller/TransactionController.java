package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class TransactionController implements Initializable{

    @FXML
    private Pane transactionPane;

    @FXML
    private Label title;

    @FXML
    private Label description;

    @FXML
    private ImageView icon;
    
    @FXML
    private Circle iconCircle;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBinds();
	}

	private void setBinds() {
		iconCircle.layoutXProperty().bind(transactionPane.prefWidthProperty().multiply(0.120));
		icon.layoutXProperty().bind(iconCircle.layoutXProperty().subtract(8));
		title.layoutXProperty().bind(transactionPane.prefWidthProperty().multiply(0.224));
		description.layoutXProperty().bind(transactionPane.prefWidthProperty().multiply(0.224));
	}

	public void setValues(String title2, String description2, String imgSrc) {
		title.setText(title2);
		description.setText(description2);
		Image image = new Image(getClass().getResourceAsStream("/images/icons/" + imgSrc));
		icon.setImage(image);
	}
    
    

}
