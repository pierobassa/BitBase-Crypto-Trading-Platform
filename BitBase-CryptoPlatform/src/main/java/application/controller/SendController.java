package application.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import application.MsgDialog;
import application.SendWithdrawDialog;
import application.model.ClientLogic;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SendController implements Initializable{

    @FXML
    private Pane backbtn;

    @FXML
    private Pane sendBtn;

    @FXML
    private TextField accountHolderField;

    @FXML
    private TextField amountField;

    @FXML
    private Label errorLbl;

    @FXML
    private ImageView sendImg;

    @FXML
    private ComboBox<String> assetsComboBox;

    @FXML
    private Label availableLbl;

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
					if(!Character.isLetter(accountHolder.charAt(accountHolder.length()-1)))
						accountHolderField.setText(accountHolderField.getText().substring(0, accountHolderField.getText().length()-1));
				}
			}
		});	
		
		amountField.lengthProperty().addListener(new ChangeListener<Number>() {
			
			final int LIMIT = 10;

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
                    if (amountField.getText().length() >= LIMIT) {

                        // if it's 11th character then just setText to previous
                        amountField.setText(amountField.getText().substring(0, LIMIT));
                    }
                    
                    String txt = amountField.getText();
                    
                    if(!Character.isDigit(txt.charAt(txt.length()-1)) && txt.charAt(txt.length()-1) != '.') //Characters that are not digits are not valid, besides '.'
                    	amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(txt.length()==1 && txt.charAt(0)=='.') //Can't start with '.'
                    	amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(txt.chars().filter(ch -> ch == '.').count() == 2) //Check if user inserted another '.' which is not a valid number format. Ex: '2.2412.' is invalid
                    	 amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(txt.chars().filter(ch -> ch == '.').count() == 1) { //Only two digits after the '.' are allowed
						int pos = 0;
						for(int i=0; i<txt.length(); i++) {
							if(txt.charAt(i) == '.') {
								pos = i;
								break;
							}
						}
						if(txt.substring(pos, txt.length()-1).length() > 5)
							 amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
					}
                }
			}
		});
	}

	private void initValues() {
		Image image = new Image(getClass().getResourceAsStream("/images/send.png"));
	    sendImg.setImage(image);
	    availableLbl.setText(null);
	    errorLbl.setText(null);		
	    
	    ObservableList<String> assets = FXCollections.observableArrayList();
	    
	    for(var key : SupportedAssets.getInstance().getAssets().keySet())
	    	if(key!=Protocol.USD)
	    		assets.add(key);
	    
	    
	    assetsComboBox.setItems(assets);
	    
	    assetsComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
	    	Double owned = ClientLogic.getInstance().getAmountOwned(assetsComboBox.getSelectionModel().getSelectedItem());
	    	
	    	if(owned!=null) {
	    		BigDecimal d = new BigDecimal(owned);
			
	    		d = d.setScale(5, RoundingMode.DOWN); //Otherwise there are too many decimal units
			
	    		availableLbl.setText("Available: " + d.toPlainString() + " " + assetsComboBox.getSelectionModel().getSelectedItem());
	    	
	    	}
	    }); 
	}

	@FXML
	public void goBack() throws IOException {
		SendWithdrawDialog.getInstance().goBack();
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	@FXML
	public void send() throws IOException {
		if(!checkConstraints()) {
			return;
		}
		errorLbl.setText(null);
		Double amount = Double.valueOf(amountField.getText());
		String asset = assetsComboBox.getSelectionModel().getSelectedItem();
		String recipient = accountHolderField.getText();
		Double total = amount*Prices.getInstance().getCryptoCurrentPrice(asset);
		total = round(total, 3);
		
		ClientLogic.getInstance().send(amount, total, asset, recipient);
		ClientLogic.getInstance().setTemporaryTransaction(amount, total, asset, Protocol.SEND, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString(), recipient);
		MsgDialog.getInstance().showPendingDialog();
	}

	private boolean checkConstraints() {
		if(accountHolderField.getText().isEmpty() || amountField.getText().isEmpty()) {
			errorLbl.setText("Please fill all fields to proceed.");
			return false;
		}
		if(assetsComboBox.getSelectionModel().isEmpty()) {
			errorLbl.setText("Please select an asset to send.");
			return false;
		}
		
		Double amountDbl = Double.valueOf(amountField.getText());
		if(amountDbl.equals(0.0)) {
			errorLbl.setText("Please insert an amount greater than zero.");
			return false;
		}
		if(amountDbl>ClientLogic.getInstance().getAmountOwned(assetsComboBox.getSelectionModel().getSelectedItem())) {
			errorLbl.setText("I'm sorry! Insufficient funds.");
			return false;
		}
		if(accountHolderField.getText().equals(ClientLogic.getInstance().getUsername())) {
			errorLbl.setText("I'm sorry! You can't send to yourself!");
			return false;
		}
		
		return true;
	}

	public void resetInfo() {
		amountField.setText("");
		accountHolderField.setText("");
		assetsComboBox.getSelectionModel().clearSelection();
	    assetsComboBox.setPromptText("Asset");
		
		
		availableLbl.setText("");
	}
}
