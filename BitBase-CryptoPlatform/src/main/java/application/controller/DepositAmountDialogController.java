package application.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import application.DepositDialog;
import application.SceneHandler;
import application.model.ClientLogic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DepositAmountDialogController implements Initializable{
	
	 @FXML
	    private Pane closeBtn;

	    @FXML
	    private Pane nextBtn;

	    @FXML
	    private Label errorLbl;

	    @FXML
	    private Label priceLbl;

	    @FXML
	    private Label balanceLbl;

	    @FXML
	    private Pane amount1000Btn;

	    @FXML
	    private Pane amount500Btn;

	    @FXML
	    private Pane amount250Btn;

	    @FXML
	    private Pane amount100Btn;

	    @FXML
	    private TextField amountField;

	    @FXML
	    private ImageView balanceImg;

	    private Double deposit;
	    

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			initValues();
			setConstraints();
			initImages();
			updateBalance();
		}
		
		
		private void initImages() {
			Image image = new Image(getClass().getResourceAsStream("/images/icons/wallet-icon-deposit.png"));
			balanceImg.setImage(image);
		}


		private void setConstraints() {
			amountField.lengthProperty().addListener(new ChangeListener<Number>() {
	    		
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					String amount = amountField.getText();	
					if (newValue.intValue() > oldValue.intValue()) {
						if(amount.charAt(0)!='$')
							amountField.setText('$'+amount);
						if(amount.length()>1 && amount.charAt(0) != '$') {
							//amountField.setText("$"+amount);
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
		}


		private void initValues() {
			errorLbl.setText("");
		}


		@FXML
	    public void closeDialog(MouseEvent event) {
			closeBtn.getScene().getWindow().hide();
			SceneHandler.getInstance().removeSceneBlur();
	    }
		
		@FXML
		public void goToPayment() throws IOException {
			if(!checkConstaints())
				return;
			
			errorLbl.setText(null);
			DepositDialog.getInstance().goToPayment(deposit);
		}
		
		private boolean checkConstaints() {
			try {
				String amount = amountField.getText();
				if(amount.length()<2) {
					errorLbl.setText("Please insert the amount you want to deposit.");
					return false;
				}
				if(amount.charAt(0)!='$') {
					amountField.setText("$"+amount);
					amount = "$" + amount;
				}
				amount = amount.substring(1);
				deposit = Double.valueOf(amount);
				if(deposit.equals(0.0)){
					errorLbl.setText("Please insert an amount greater than zero.");
					return false;
				}
				if(deposit>1000000) {
					errorLbl.setText("Maximum deposit amount is 1 Million USD.");
					return false;
				}
				return true;
			}
			catch (NumberFormatException e) {
				errorLbl.setText("Please insert a valid deposit amount.");
				return false;
			}

		}


		@FXML
		public void set1000() {
			setAmount(Double.valueOf(1000));
		}
		
		@FXML
		public void set500() {
			setAmount(Double.valueOf(500));
		}
		
		@FXML
		public void set250() {
			setAmount(Double.valueOf(250));
		}
		
		@FXML
		public void set100() {
			setAmount(Double.valueOf(100));
		}


		private void setAmount(Double amount) {
			amountField.setText("$"+amount);
		}


		public void updateBalance() {
			Double balance = ClientLogic.getInstance().getBalance();
			BigDecimal d = new BigDecimal(balance);
			
			d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
			
			balanceLbl.setText("$"+d.toPlainString());			
			
			amountField.setText("");
			amountField.requestFocus();
		}
}
