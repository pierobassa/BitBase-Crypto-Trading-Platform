package application.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import application.SceneHandler;
import application.model.ClientLogic;
import application.MsgDialog;
import application.net.client.ClientService;
import application.net.common.Protocol;
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
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable {

	@FXML
    private AnchorPane anchor;
	
	@FXML
    private ImageView topImage;

    @FXML
    private ImageView rightImage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLbl;

    @FXML
    private Label signupLbl;

    @FXML
    private Button loginBtn;


	@Override
	public void initialize(URL location, ResourceBundle resources) {     
        inits();
        setImages();
	}
	
	private void inits() {
		errorLbl.setText("");
		usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.TAB)
					passwordField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						login();
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
					usernameField.requestFocus();
				if(event.getCode() == KeyCode.ENTER) {
					try {
						login();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
		
		
	}

	private void setImages() {
		Image image = new Image(getClass().getResourceAsStream("/images/final.png"));
        rightImage.setImage(image);		
        
        image = new Image(getClass().getResourceAsStream("/images/onlyB.png"));
        topImage.setImage(image);		
	}

	private void authentication() throws IOException {
    	if(!netIsAvailable()) { //Checks if there is connection to the internet 
    		MsgDialog.getInstance().showError("Please connect to the internet.");
	    	
	    	ClientService.getInstance().resetClient();
	    	return;
    	}
    	
    	String res = ClientService.getInstance().authenticationLogin(usernameField.getText(), passwordField.getText());
    	
    	String username = usernameField.getText();
    
    	if(res.equals(Protocol.OK)) { //username and password are valid
    		try {
    			ClientLogic.getInstance().setUsername(username);
    			SceneHandler.getInstance().setLoadingScene();

    	    	ClientService.getInstance().setOnSucceeded(new ClientOnSucceededController());

    	    	usernameField.setText("");
    	    	passwordField.setText("");
    	    	
    	    	ClientService.getInstance().start();    	
    	    	
			} catch (Exception e) {
				System.out.println(e);
				MsgDialog.getInstance().showError("error while loading home screen");
				ClientService.getInstance().resetClient();
				SceneHandler.getInstance().setLoginScene();
			}
    	}
    	else {
    		MsgDialog.getInstance().showError(res);
    		passwordField.setText("");
    		ClientService.getInstance().resetClient();
    	}
    }
	
	private static boolean netIsAvailable() {
		final URLConnection conn;
	    try {
	        final URL url = new URL("http://www.google.com");
	        conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        return false;
	    } catch (IOException e) {

	        return false;
	    }
	}
	
	@FXML
	private void switchToSignup() {
		try {
			SceneHandler.getInstance().setSignupScene();
			usernameField.setText(null);
			passwordField.setText(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void login() throws IOException {
		errorLbl.setText("");
		if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			errorLbl.setText("Please insert both username and password");
			return;
		}
		authentication();
	}
		
	
	public void authenticationError() {
		errorLbl.setText("I'm sorry! Incorrect username or password.");
	}
	
}
