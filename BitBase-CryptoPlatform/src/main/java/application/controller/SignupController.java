package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.MsgDialog;
import application.SceneHandler;
import application.net.client.ClientService;
import application.net.common.Protocol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode; 
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SignupController implements Initializable {


	@FXML
    private ImageView topImage;

    @FXML
    private ImageView infoIcon;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private ImageView leftImage;

    @FXML
    private Button signupBtn1;

    @FXML
    private Label errorLbl;

    @FXML
    private Button signupBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initImages();
		inits();
	}
	
	private void inits() {
		errorLbl.setText("");		
		
		usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					nameField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						register();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					surnameField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						register();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		surnameField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					passwordField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						register();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		
		passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					repeatPasswordField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						register();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		
		repeatPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					usernameField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						register();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		usernameField.lengthProperty().addListener(new ChangeListener<Number>() {
			

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
                    String username = usernameField.getText();
                    
                    if(username.length()==1 && !Character.isLetter(username.charAt(username.length()-1)))  //USERNAME CAN'T START WITH SOMETHING DIFFERENT THAN A NUMBER
                    	usernameField.setText(usernameField.getText().substring(0, usernameField.getText().length()-1));   
                    else if(!Character.isLetter(username.charAt(username.length()-1)) && !Character.isDigit(username.charAt(username.length()-1))) //USERNAME CAN'T CONTAIN SPECIAL CHARS. BESIDES NUMBERS AND LETTERS
                    	usernameField.setText(usernameField.getText().substring(0, usernameField.getText().length()-1));   
				}
			}
		});
	}

	private void initImages() {
		Image image = new Image(getClass().getResourceAsStream("/images/passwordRequirements.png"), 300, 180, true, true);
		
		
		image = new Image(getClass().getResourceAsStream("/images/onlyB.png"));
        topImage.setImage(image);
        
        image = new Image(getClass().getResourceAsStream("/images/final.png"));
        leftImage.setImage(image);
        
        image = new Image(getClass().getResourceAsStream("/images/icons/info-icon.png"));
        infoIcon.setImage(image);
	}

	@FXML
	private void authenticationSignup() throws IOException {
    	String res = ClientService.getInstance().authenticationSignup(usernameField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText());
    	if(res.equals(Protocol.OK)) {
    		try {
    			MsgDialog.getInstance().showSuccess("Registration complete! Login to continue.");
    			SceneHandler.getInstance().setLoginScene();
    			ClientService.getInstance().resetClient();
    			
    			resetValues();
			} catch (Exception e) {
				MsgDialog.getInstance().showError("error while loading login screen");
			}
    	}
    	else {
    		MsgDialog.getInstance().showError(res);
    		ClientService.getInstance().resetClient();
    	}
    }
	
	private void resetValues() {
		usernameField.setText(null);
		nameField.setText(null);
		surnameField.setText(null);
		passwordField.setText(null);
		repeatPasswordField.setText(null);
	}

	public void authenticationError() {
		errorLbl.setText("I'm sorry! User already exists.");
	}
	
	private boolean allFilled() {
		if(usernameField.getText().isEmpty() || nameField.getText().isEmpty() 
				|| surnameField.getText().isEmpty() || passwordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty()) {
			errorLbl.setText("I'm sorry! All fields need to be filled.");
			return false;
		}
		return true;
	}

	private boolean passwordRequirements() {
		String password = passwordField.getText();			
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		return password.matches(pattern);	
	}

	@FXML
	public void register() throws IOException {
		errorLbl.setText("");
		if(!allFilled() || !passwordsMatch()) {
			return;
		}
		if(!passwordRequirements()) {
			errorLbl.setText("I'm sorry! The password you inserted does not match the requirements.");
			return;
		}
		authenticationSignup();
	}

	private boolean passwordsMatch() {
		if(!repeatPasswordField.getText().equals(passwordField.getText())) {
			errorLbl.setText("Unfortunately the two passwords do not match!");
			return false;
		}
		return true;
	}

	public void openPasswordRequirements() throws IOException {
		MsgDialog.getInstance().showInfo(3);
	}
	
	@FXML
    void goBack(MouseEvent event) {
		resetValues();
		SceneHandler.getInstance().setLoginScene();
    }


	

	
}
