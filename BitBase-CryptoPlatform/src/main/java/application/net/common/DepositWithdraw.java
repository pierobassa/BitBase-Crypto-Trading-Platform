package application.net.common;

import java.io.Serializable;

public class DepositWithdraw implements Serializable{ //Handles a deposit or withdraw of FIAT money
	
	private static final long serialVersionUID = -3724502676057490356L;
	private String username;
	private Double depositAmount;
	private Boolean deposit; //True if deposit, false if withdrawal
	
	public DepositWithdraw(String username, Double depositAmount, Boolean deposit) {
		this.username = username;
		this.depositAmount = depositAmount;
		this.setDeposit(deposit);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Boolean getDeposit() {
		return deposit;
	}

	public void setDeposit(Boolean deposit) {
		this.deposit = deposit;
	}
}
