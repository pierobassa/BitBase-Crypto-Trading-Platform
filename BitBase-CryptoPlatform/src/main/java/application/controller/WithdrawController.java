package application.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import application.MsgDialog;
import application.SendWithdrawDialog;
import application.model.ClientLogic;
import application.net.common.Protocol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WithdrawController implements Initializable{

    @FXML
    private Pane backbtn;

    @FXML
    private Pane withdrawBtn;

    @FXML
    private TextField accountHolderField;

    @FXML
    private TextField ibanField;

    @FXML
    private Label errorLbl;

    @FXML
    private ImageView withdrawImg;

    @FXML
    private TextField amountField;

    @FXML
    private Label balanceLbl;

    @FXML
    private ImageView balanceImg;
    
    private Double withdraw;

    @Override
	public void initialize(URL location, ResourceBundle resources) {
	    initValues();
	    setConstraints();
	}
	
	private void setConstraints() {
		accountHolderField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String accountHolder = accountHolderField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(!Character.isLetter(accountHolder.charAt(accountHolder.length()-1)) && accountHolder.charAt(accountHolder.length()-1) != ' ')
						accountHolderField.setText(accountHolderField.getText().substring(0, accountHolderField.getText().length()-1));
				}
			}
		});	
		
		amountField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String amount = amountField.getText();	
				if (newValue.intValue() > oldValue.intValue()) {
					if(amount.charAt(0)!='$')
						amountField.setText('$'+amount);
					if(amount.length()>1 && amount.charAt(0) != '$') {
						
						amountField.setText(amount.substring(1, amount.length()));
					}
					if(!Character.isDigit(amount.charAt(amount.length()-1)) && amount.charAt(amount.length()-1) != '.')
						amountField.setText(amount.substring(0, amount.length()-1));
					if(!Character.isDigit(amount.charAt(amount.length()-1)) && amount.length()==2)
						amountField.setText(amount.substring(0, amount.length()-1));
					if(amount.chars().filter(ch -> ch == '.').count() == 2) //Check if user inserted another '.' which is not a valid number format. Ex: '2.2412.' is invalid
                    	 amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
					if(amount.chars().filter(ch -> ch == '.').count() == 1) { //Only two digits after the '.' are allowed
						int pos = 0;
						for(int i=0; i<amount.length(); i++) {
							if(amount.charAt(i) == '.') {
								pos = i;
								break;
							}
						}
						if(amount.substring(pos, amount.length()-1).length() > 2)
							 amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
					}
				}
				if(newValue.intValue() < oldValue.intValue()) {
					if(amount.length()>0)
						if(amount.charAt(0)!='$') 
							amountField.setText("$"+amount);
				}
			}
		});		
		
		ibanField.lengthProperty().addListener(new ChangeListener<Number>() {
    		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String iban = ibanField.getText();	
				if(iban.length()>0)
					if(Character.isLowerCase(iban.charAt(iban.length()-1)))
						ibanField.setText(iban.toUpperCase());
				if (newValue.intValue() > oldValue.intValue()) {
					if(iban.length()<=2) {
						if(!Character.isLetter(iban.charAt(iban.length()-1)) && iban.charAt(iban.length()-1) != ' ')
							ibanField.setText(ibanField.getText().substring(0, ibanField.getText().length()-1));
					}
				else if(!Character.isDigit(iban.charAt(iban.length()-1)) && iban.charAt(iban.length()-1) != ' ')
						ibanField.setText(ibanField.getText().substring(0, ibanField.getText().length()-1));
				}
				if(iban.length()>31)
					ibanField.setText(ibanField.getText().substring(0, ibanField.getText().length()-1));
			}
		});		
	}

	private void initValues() {
		Image image = new Image(getClass().getResourceAsStream("/images/withdraw.png"));
	    withdrawImg.setImage(image);
	    errorLbl.setText(null);
	    
	    Double balance = ClientLogic.getInstance().getBalance();
	    
	    BigDecimal d = new BigDecimal(balance);
		
		d = d.setScale(2, RoundingMode.DOWN); //Otherwise there are too many decimal unit
	    
	    balanceLbl.setText("$"+ d.toPlainString());
	    image = new Image(getClass().getResourceAsStream("/images/icons/wallet-icon-deposit.png"));
	    balanceImg.setImage(image);
	    
	}

	@FXML
	public void goBack() throws IOException {
		SendWithdrawDialog.getInstance().goBack();
	}
	
	@FXML
	public void withdraw() throws IOException {
		if(!checkConstaints())
			return;
		
		errorLbl.setText(null);
		System.out.println(withdraw);
		
		ClientLogic.getInstance().withdraw(withdraw);
		ClientLogic.getInstance().setTemporaryTransaction(withdraw, withdraw, null, Protocol.WITHDRAW, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString(), null);
		MsgDialog.getInstance().showPendingDialog();
		
	}

	private boolean checkConstaints() {
		if(accountHolderField.getText().isEmpty() || ibanField.getText().isEmpty() || amountField.getText().isEmpty()) {
			errorLbl.setText("Please fill all fields to proceed.");
			return false;
		}
		if(ibanField.getText().length()<15) { //iban number of chars is between 15 and 31
			errorLbl.setText("Please insert a valid IBAN.");
			return false;
		}
		String amount = amountField.getText();
		if(amount.charAt(0)!='$') {
			amountField.setText("$"+amount);
			amount = "$" + amount;
		}
		amount = amount.substring(1);
		Double withdraw = Double.valueOf(amount);
		if(withdraw.equals(0.0)){
			errorLbl.setText("Please insert an amount greater than zero.");
			return false;
		}
		if(withdraw>ClientLogic.getInstance().getBalance()) {
			errorLbl.setText("I'm sorry! Insufficient funds.");
			return false;
		}
		
		this.withdraw = withdraw;
		return true;
	}

	public void updateBalance() {
		Double balance = ClientLogic.getInstance().getBalance();
		BigDecimal d = new BigDecimal(balance);
			
		d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
			
		balanceLbl.setText("$"+d.toPlainString());			
	}
	
	public void resetInfo() {
		accountHolderField.setText("");
		ibanField.setText("");
		amountField.setText("");
	}

}
