package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.MsgDialog;
import application.SceneHandler;
import application.model.ClientLogic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MyAccountController implements Initializable{


	@FXML
    private Pane upperContainer;

    @FXML
    private TextField usernameField;

    @FXML
    private ImageView img;

    @FXML
    private ImageView editIcon;

    @FXML
    private Pane closeBtn;

    @FXML
    private Pane saveBtn;

    @FXML
    private ImageView saveIcon;

    @FXML
    private Pane changePasswordBtn;

    @FXML
    private Pane oldPasswordPane;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private Pane newPasswordPane;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label errorLbl;

    @FXML
    private Pane infoPasswordBtn;

    @FXML
    private ImageView infoPasswordIcon;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPositions();
		initValues();
		initImages();
		setConstraints();
	}
	
	private void setConstraints() {
		usernameField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String username = usernameField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isLetter(username.charAt(username.length()-1)))
						usernameField.setText(usernameField.getText().substring(0, usernameField.getText().length()-1));
				}
			}
		});
	}

	private void initPositions() {
		upperContainer.setLayoutY(130);
		changePasswordBtn.layoutYProperty().bind(upperContainer.layoutYProperty().add(upperContainer.heightProperty()).add(22));
		errorLbl.layoutYProperty().bind(saveBtn.layoutYProperty().add(saveBtn.heightProperty()).add(6));
	}

	private void initImages() {
		Image image = new Image(getClass().getResourceAsStream("/images/myaccount.png"));
	    img.setImage(image);	
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/edit-icon.png"));
	    editIcon.setImage(image);	
	        
	    image = new Image(getClass().getResourceAsStream("/images/icons/confirm-icon.png"));
	    saveIcon.setImage(image);
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/info-icon.png"));
	    infoPasswordIcon.setImage(image);
	}

	private void initValues() {
		usernameField.setText(ClientLogic.getInstance().getUsername());
		errorLbl.setText(null);
		changePasswordBtn.setVisible(true);
		usernameField.deselect();
		usernameField.setCursor(Cursor.DEFAULT);
		usernameField.setFocusTraversable(false);
		usernameField.setEditable(false);
		oldPasswordPane.setVisible(false);
		newPasswordPane.setVisible(false);
		saveBtn.setVisible(false);
		infoPasswordBtn.setVisible(false);
		oldPasswordField.setText(null);
		newPasswordField.setText(null);
	}

	@FXML
    public void closeDialog(MouseEvent event) {
		reset();
		closeBtn.getScene().getWindow().hide();
		SceneHandler.getInstance().removeSceneBlur();
    }
	
	public void reset() {
		initValues();
		initPositions();
	}
	
	@FXML
	public void editUsername() {
		usernameField.setEditable(true);
		usernameField.setFocusTraversable(true);
		usernameField.setCursor(Cursor.HAND);
		saveBtn.setVisible(true);
		if(!oldPasswordPane.isVisible())
			saveBtn.layoutYProperty().bind(changePasswordBtn.layoutYProperty().add(changePasswordBtn.heightProperty()).add(26));
		else
			saveBtn.layoutYProperty().bind(newPasswordPane.layoutYProperty().add(newPasswordPane.heightProperty()).add(26));
	}
	
	@FXML
	public void changePassword() {
		changePasswordBtn.setVisible(false);
		upperContainer.setLayoutY(69);
		oldPasswordPane.setVisible(true);
		newPasswordPane.setVisible(true);
		saveBtn.setVisible(true);
		saveBtn.layoutYProperty().bind(newPasswordPane.layoutYProperty().add(newPasswordPane.heightProperty()).add(26));
		infoPasswordBtn.setVisible(true);
	}
	
	@FXML
	public void save() throws IOException {
		if(!oldPasswordPane.isVisible()) {
			if(!checkConstraintsUsername())
				return;
		
			//SAVE USERNAME TO SERVER UPDATE ECC
			MsgDialog.getInstance().showPendingDialog();
			ClientLogic.getInstance().updateUsername(usernameField.getText());
		}
		else {
			if(usernameField.isEditable()) {
				if(!checkConstraintsUsername() || !checkPasswordConstraints())
					return;
			
				//SAVE USERNAME AND PASSWORD
				MsgDialog.getInstance().showPendingDialog();
				ClientLogic.getInstance().updateUsernameAndPassword(usernameField.getText(), oldPasswordField.getText(), newPasswordField.getText());
			}
			else {
				 if(!checkPasswordConstraints())
					 return;
				 
				 //SAVE PASSWORD
				 MsgDialog.getInstance().showPendingDialog();
				 ClientLogic.getInstance().updatePassword(oldPasswordField.getText(), newPasswordField.getText());
			}
		}
	}

	private boolean checkPasswordConstraints() {
		if(oldPasswordField.getText() == null || newPasswordField.getText() == null ) {
			errorLbl.setText("I'm sorry! All fields need to be filled.");
			return false;
		}
		
		if(oldPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty()) {
			errorLbl.setText("Please insert old and new password.");
			return false;
		}
		
		if(oldPasswordField.getText().equals(newPasswordField.getText())) {
			errorLbl.setText("I'm sorry! Old and new password need to be different.");
			return false;
		}	
		
		String password = newPasswordField.getText();			
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		if(!password.matches(pattern)) {
			errorLbl.setText("I'm sorry! New password does not satisfy the requirements.");
			return false;
		}
		
		return true;
	}

	private boolean checkConstraintsUsername() {
		if(usernameField.getText().equals(ClientLogic.getInstance().getUsername())) {
			errorLbl.setText("Please insert a different username than your current.");
			return false;
		}
		if(usernameField.getText().isEmpty()) {
			errorLbl.setText("Please insert your new username.");
			return false;
		}
		return true;
	}
	
	@FXML
	public void infoPasswordRequirements() throws IOException {
		MsgDialog.getInstance().showInfo(3);
	}

}
