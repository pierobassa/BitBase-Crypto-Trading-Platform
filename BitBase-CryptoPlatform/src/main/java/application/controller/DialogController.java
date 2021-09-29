package application.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import application.net.common.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class DialogController{

	@FXML
    private ImageView img;

    @FXML
    private Label dialogLbl;

    @FXML
    private Pane closeBtn;

    @FXML
    private ImageView receivedImg;
    
	@FXML
    public void closeDialog() {
		reset();
    	closeBtn.getScene().getWindow().hide();
	}
    
    private void reset() {
    	img.setVisible(true);
    	dialogLbl.setLayoutY(107);
    	dialogLbl.setPrefHeight(68);
    	closeBtn.setLayoutY(180);
	}

	public void setText(String text) {
    	dialogLbl.setText(text);
    }

	public void setPendingDialog() {
		
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		setText("Please wait. Your order is being processed");
		Image image = new Image(getClass().getResourceAsStream("/images/icons/loading-icon.gif"));
        img.setImage(image);
		closeBtn.setVisible(false);
		receivedImg.setImage(null);
	}

	public void setTransactionSuccessfulDialog() {
		
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		setText("Your order is complete!");
		Image image = new Image(getClass().getResourceAsStream("/images/icons/success-icon.png"));
        img.setImage(image);
		closeBtn.setVisible(true);
		receivedImg.setImage(null);
	}

	public void setError(String text) {
		
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		setText(text);
		Image image = new Image(getClass().getResourceAsStream("/images/icons/failed-icon.png"));
        img.setImage(image);
        closeBtn.setVisible(true);
        receivedImg.setImage(null);
	}

	public void setShowInfo(int info) {
		Image image = new Image(getClass().getResourceAsStream("/images/icons/information-icon.png"));
        img.setImage(image);
        
        dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 15));
        
        switch(info) {
	        case 0:{
	        	setText("Market Cap = Current Price x Circulating Supply\n"
	        			+ "Refers to the total market value of a cryptocurrency’s circulating supply. (Value in Billions)");
	        	break;
	        }
	        case 1:{
	        	setText("The amount of coins that are circulating in the market and are in public hands. It is analogous to the flowing shares in the stock market. (Value in Millions)");
	        	break;
	        }
	        case 2:{
	        	setText("A measure of how much of the cryptocurrency was traded in the last 24 hours. (Value in Billions)");
	        	break;
	        }
	        case 3:{
	        	setText("- At least 8 characters\n- At least one upper and lower case character\n- At least one special character (@#$%^&+=)\n- At least one number\n- No spaces allowed");
	        	dialogLbl.setPrefHeight(100);
	        	dialogLbl.setLayoutY(40);
	        	img.setVisible(false);
	        	closeBtn.setLayoutY(160);
	        	break;
	        }
	        case 4:{
	        	setText("Please check your connection speed to the internet or retry later.");
	        	break;
	        }
        }
        
        closeBtn.setVisible(true);
        receivedImg.setImage(null);
	}

	public void setWithdrawSuccessfulDialog() {
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		setText("Your withdraw order is complete!");
		Image image = new Image(getClass().getResourceAsStream("/images/icons/success-icon.png"));
        img.setImage(image);
		closeBtn.setVisible(true);
		receivedImg.setImage(null);
	}

	public void setSuccess(String text) {
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		setText(text);
		Image image = new Image(getClass().getResourceAsStream("/images/icons/success-icon.png"));
        img.setImage(image);
		closeBtn.setVisible(true);
		receivedImg.setImage(null);
	}

	public void setReceivedAsset(Transaction transaction) {
		dialogLbl.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 19));
		
		BigDecimal d = new BigDecimal(transaction.getAmount());
		
		d = d.setScale(5, RoundingMode.DOWN); //Otherwise it would be in a scientific notation
		
		setText("You received " + d.toPlainString() + " " +  transaction.getCrypto() + " from " + transaction.getUsername() + "!");
		img.setImage(null);
		Image image = new Image(getClass().getResourceAsStream("/images/icons/received.gif"));
		closeBtn.setVisible(true);
		receivedImg.setImage(image);
	}

	
}
