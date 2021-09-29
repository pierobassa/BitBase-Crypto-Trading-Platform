package application.net.common;

import java.io.Serializable;

public class Transaction implements Serializable { //Handles a transaction

	private static final long serialVersionUID = 235690879766008148L;
	
	private double amount;
	private double total;
	private String crypto;
	private String buy; 
	private String username;
	private String date;
	private String recipient; //If the transaction is a SEND transactions to another user
	
	public Transaction(double amount, double total, String crypto, String buy, String username, String date, String recipient) {
		this.amount = amount;
		this.total = total;
		this.crypto = crypto;
		this.buy = buy;
		this.username = username;
		this.date = date;
		this.setRecipient(recipient);
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTotal() {
		return total;
	}
	public void setPrice(double total) {
		this.total = total;
	}
	public String getCrypto() {
		return crypto;
	}
	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}
	public String getBuy() {
		return buy;
	}
	public void setBuy(String buy) {
		this.buy = buy;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	
}
