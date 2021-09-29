package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.DepositDialog;
import application.MsgDialog;
import application.model.ClientLogic;
import application.net.common.Protocol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DepositDialogController implements Initializable{

    @FXML
    private Pane cancelBtn;

    @FXML
    private Pane depositBtn;

    @FXML
    private ImageView cardImg;

    @FXML
    private Label cardNameLbl;

    @FXML
    private Label cardNumberPrivateLbl;

    @FXML
    private Label supplyLbl2;

    @FXML
    private Label supplyLbl21;

    @FXML
    private Label validLbl;

    @FXML
    private Label cvvLbl;

    @FXML
    private Label cardLast4NumbersLbl;
    
    @FXML
    private Label errorLbl;

    @FXML
    private TextField cardNameField;

    @FXML
    private TextField cardNumber;

    @FXML
    private TextField expirationField;

    @FXML
    private PasswordField cvvField;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setInitValues();
		setDynamicCard();		
	}
    
    
    public void setInitValues() {
		cardNameLbl.setText("");
		cardNumberPrivateLbl.setText("");
		cardLast4NumbersLbl.setText("");
		validLbl.setText("");
		cvvLbl.setText("");
		errorLbl.setText("");
		cardNameField.setText("");
		cardNumber.setText("");
		expirationField.setText("");
		cvvField.setText("");
	}


	private void setDynamicCard() { //Live card based on input values. Fields are constrained to allow only specific characters.
    	cardNameField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String cardName = cardNameField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isLetter(cardName.charAt(cardName.length()-1)) && cardName.charAt(cardName.length()-1) != ' ')
						cardNameField.setText(cardNameField.getText().substring(0, cardNameField.getText().length()-1));
					else
						cardNameLbl.setText(cardNameField.getText());
				}
				if(newValue.intValue() < oldValue.intValue()) {
					cardNameLbl.setText(cardNameField.getText());
				}
			}
		});		
    	
    	cardNumber.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isDigit(cardNumber.getText().charAt(cardNumber.getText().length()-1))) //Characters that are not digits are not valid, besides '.'
						cardNumber.setText(cardNumber.getText().substring(0, cardNumber.getText().length()-1));
					
					else if(cardNumber.getText().length()<=12) {
						cardNumberPrivateLbl.setText(cardNumberPrivateLbl.getText() + "*");
						if(cardNumber.getText().length() % 4 == 0)
							cardNumberPrivateLbl.setText(cardNumberPrivateLbl.getText() + " ");
					}
					else if(cardNumber.getText().length()>12  && cardNumber.getText().length()<=16) {
						cardLast4NumbersLbl.setText(cardLast4NumbersLbl.getText() + cardNumber.getText().charAt(cardNumber.getText().length()-1));
					}
					else if(cardNumber.getText().length()>16) {
						cardNumber.setText(cardNumber.getText().substring(0, cardNumber.getText().length()-1));
					}
					if(cardNumber.getText().length()!=0) {
						if(cardNumber.getText().charAt(0)=='4' || cardNumber.getText().charAt(0)=='5' ) {
							Image image = null;
							if(cardNumber.getText().charAt(0) == '4') 
								image = new Image(getClass().getResourceAsStream("/images/icons/visa-icon.png"));
							if(cardNumber.getText().charAt(0) == '5')
								image = new Image(getClass().getResourceAsStream("/images/icons/mastercard-icon.png"));
							cardImg.setImage(image);
						}
						else
							cardImg.setImage(null);
					}
					if(cardNumber.getText().length()>17)
						cardNumber.setText("");
				}
				if (newValue.intValue() < oldValue.intValue()) {
					if(cardNumber.getText().length()==0) {
						cardImg.setImage(null);
						cardLast4NumbersLbl.setText("");
					}
					if(cardNumber.getText().length()<=12) {
						int x = cardNumber.getText().length();
						String privates = "";
						for(int i=1; i<=x; i++) {
							privates += "*";
							if(i%4==0)
								privates += " ";
						}
						cardNumberPrivateLbl.setText(privates);
					}
					if(cardNumber.getText().length()>=12 && cardNumber.getText().length()<=16) {
						cardLast4NumbersLbl.setText(cardNumber.getText().substring(12));
					}
					
				}
			}
		});		
    	
    	expirationField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String expiration = expirationField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isDigit(expiration.charAt(expiration.length()-1)) && expiration.length()<=2)
						expirationField.setText(expirationField.getText().substring(0, expirationField.getText().length()-1));
					else if(expiration.length() == 3 && expiration.charAt(expiration.length()-1) != '/')
						expirationField.setText(expirationField.getText().substring(0, expirationField.getText().length()-1));
					else if((!Character.isDigit(expiration.charAt(expiration.length()-1)) && expiration.length()>=4) || expiration.length()>5)
    					expirationField.setText(expirationField.getText().substring(0, expirationField.getText().length()-1));
					else
						validLbl.setText(expirationField.getText());
				}
				if(newValue.intValue() < oldValue.intValue()) {
					validLbl.setText(expirationField.getText());
				}
			}
		});	
    	
    	cvvField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String cvv = cvvField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isDigit(cvv.charAt(cvv.length()-1)) || cvv.length() > 3)
						cvvField.setText(cvvField.getText().substring(0, cvvField.getText().length()-1));
					else
						cvvLbl.setText(cvvLbl.getText() + "*");
				}
				if(newValue.intValue() < oldValue.intValue()) {
					int x = cvvField.getText().length();
					String privates = "";
					for(int i=1; i<=x; i++) {
						privates += "*";
					}
					cvvLbl.setText(privates);
				}
			}
		});		
	}


	@FXML
    public void goBack(MouseEvent event) throws IOException {
    	DepositDialog.getInstance().goBack();
	}
	
	@FXML
	public void deposit() throws IOException {
		if(!checkConstaints())
			return;
		
		errorLbl.setText(null);
		ClientLogic.getInstance().deposit(DepositDialog.getInstance().getDepositAmount());		
		ClientLogic.getInstance().setTemporaryTransaction(DepositDialog.getInstance().getDepositAmount(), DepositDialog.getInstance().getDepositAmount(), null, Protocol.DEPOSIT, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString(), null);
		MsgDialog.getInstance().showPendingDialog();
	}


	private boolean checkConstaints() {
		if(cardNumber.getText().isEmpty() || cardNameField.getText().isEmpty() || expirationField.getText().isEmpty() || cvvField.getText().isEmpty()) {
			errorLbl.setText("Please fill all fields to proceed.");
			return false;
		}
			
		if(cardNumber.getText().length()<16) {
			errorLbl.setText("Please insert a valid card number.");
			return false;
		}
		
		if(cvvField.getText().length()<3) {
			errorLbl.setText("Please insert a valid CVV.");
			return false;
		}
		
		if(expirationField.getText().length()<5) {
			errorLbl.setText("Please insert a valid expiration date.");
			return false;
		}
		
		String validity = expirationField.getText();
		String[] parts = validity.split("/");
		
		String todayDate = java.time.LocalDate.now().toString();
		String YY = todayDate.substring(2,4);
		String MM = todayDate.substring(5,7);
		double YYdbl = Double.valueOf(YY);
		double MMdbl = Double.valueOf(MM);
		
		double expYY = Double.valueOf(parts[1]);
		double expMM = Double.valueOf(parts[0]);
		
		if(expMM<0 || expMM>12){
			errorLbl.setText("Please insert a valid month.");
			return false;
		}
		if(expYY<YYdbl) {
			errorLbl.setText("Unforunately your card has expired.");
			return false;
		}
		if(expYY == YYdbl && expMM<MMdbl) {
			errorLbl.setText("Unforunately your card has expired.");
			return false;
		}
		return true;
	}
	
}

