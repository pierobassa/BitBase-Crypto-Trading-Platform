package application.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import application.ClientView;
import application.DepositDialog;
import application.MsgDialog;
import application.MyAccountDialog;
import application.SendWithdrawDialog;
import application.model.ClientLogic;
import application.model.Prices;
import application.net.client.ClientService;
import application.net.common.Message;
import application.net.common.Protocol;
import application.net.common.Stats;
import application.net.common.Transaction;
import javafx.animation.PauseTransition;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class ClientOnSucceededController implements EventHandler<WorkerStateEvent>{ //Class that handles the various messages that the client receives from the server

	@Override
	public void handle(WorkerStateEvent event) {		
		Message mess = (Message) event.getSource().getValue();
		switch (mess.getHeader()) {	
			case Protocol.TRANSACTION_SUCCESSFUL:{
				try {
					handleSuccessfulTransaction(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.TRANSACTION_FAILED:{
				try {
					handleFailedTransaction();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.BALANCE_RESPONSE:{
				try {
					handleBalanceResponse(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.BALANCE_FAILED:{
				handleBalanceRequestFailed();
				break;
			}
			case Protocol.AMOUNT_OWNED_RESPONSE:{
				handleAmountOwned(mess.getInformation());
		
				break;
			}
			case Protocol.AMOUNT_OWNED_FAILED:{
				handleAmountOwnedRequestFailed();
				break;
			}
			case Protocol.DEPOSIT_RESPONSE:{
				try {
					handleDeposit(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.DEPOSIT_FAILED:{
				handleDepositFailed();
				break;
			}
			case Protocol.TRANSACTIONS_FAILED:{
				handleTransactionsFailed();
				break;
			}
			case Protocol.TRANSACTIONS_RESPONSE:{
				try {
					handleTransactionsResponse(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.WITHDRAW_RESPONSE:{
				try {
					handleWithdrawResponse(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.WITHDRAW_FAILED:{
				try {
					handleWithdrawFailed();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.USER_INEXISTENT:{
				try {
					handleUserInexistent();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.RECEIVED_ASSET:{
				handleReceivedAsset(mess.getInformation());
				break;
			}
			case Protocol.SEND_RESPONSE:{
				handleSendResponse(mess.getInformation());
				break;
			}
			case Protocol.SEND_FAILED:{
				try {
					handleSendFailed();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.UPDATE_USERNAME_FAILED:{
				try {
					handleUsernameUpdateFailed();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.UPDATE_USERNAME_RESPONSE:{
				try {
					handleUsernameUpdate(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.USER_EXISTS_ERROR:{
				try {
					handleUserExists();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.PASSWORD_INVALID:{
				try {
					handlePasswordInvalid();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.UPDATE_USERNAME_PASSWORD_RESPONSE:{
				try {
					handleUsernameUpdate(mess.getInformation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.PASSWORD_CHANGE_FAILED:{
				try {
					handlePasswordChangeFailed();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.UPDATE_PASSWORD_RESPONSE:{
				try {
					handleUpdatePasswordResponse();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.CRYPTO_STATS_REQUEST_FAILED:{
				try {
					handleCryptoStatsFailed();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case Protocol.CRYPTO_STATS_RESPONSE:{
				handleCryptoStatsResponse(mess.getInformation());
				break;
			}
		}
		
		try {
			ClientService.getInstance().reset();
			ClientService.getInstance().restart();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void handleCryptoStatsResponse(Object information) { //Update the crypto stats for each asset
		@SuppressWarnings("unchecked")
		Map<String, Stats> cryptoStats = (Map<String, Stats>) information;
		for(var key : cryptoStats.keySet()) {
			Prices.getInstance().setStats(key,  cryptoStats.get(key));
			ClientLogic.getInstance().setCryptoStatsReady(key, true);
		}	
	}


	private void handleCryptoStatsFailed() throws IOException {
		MsgDialog.getInstance().showError("Server is updating. Retry later.");
		ClientLogic.getInstance().resetClientLogic();
	}


	private void handleUpdatePasswordResponse() throws IOException {
		MsgDialog.getInstance().showSuccess("Password successfully updated! Don't forget it.");
		MyAccountDialog.getInstance().reset();
	}


	private void handlePasswordChangeFailed() throws IOException {
		MsgDialog.getInstance().showError("I'm sorry! An error occurred updating your password. Please try again.");
	}


	private void handlePasswordInvalid() throws IOException {
		MsgDialog.getInstance().showError("Old password does not match.");
	}

	private void handleUserExists() throws IOException {
		MsgDialog.getInstance().showError("I'm sorry! The username is already taken.");
	}

	private void handleUsernameUpdate(Object information) throws IOException { //Username successfully updated
		String newUsername = (String) information;
		ClientLogic.getInstance().setUsername(newUsername);
		MsgDialog.getInstance().showSuccess("Changes successfully applied!");
		ClientView.getInstance().changeUsername(newUsername);
		MyAccountDialog.getInstance().reset();
	}

	private void handleUsernameUpdateFailed() throws IOException {
		MsgDialog.getInstance().showError("I'm sorry! An error occurred. Please try again.");
	}

	private void handleSendFailed() throws IOException {
		MsgDialog.getInstance().showError("I'm sorry! Send failed. Please try again.");
	}

	private void handleSendResponse(Object information) { //When sending an asset was successfully completed
		@SuppressWarnings("unchecked")
		Map<String, Double> amountOwned = (Map<String, Double>) information;
		
		PauseTransition pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(e -> {
		    try {
				handleAmountOwned(amountOwned);
				
				SendWithdrawDialog.getInstance().goBack();
		    	SendWithdrawDialog.getInstance().resetInfo();
		    	
		    	ClientView.getInstance().updateBalance();
		    	ClientLogic.getInstance().updatePortfolioValue();
		    	ClientView.getInstance().updatePieChart();
		    	ClientLogic.getInstance().addTransaction();
		    	ClientLogic.getInstance().updatePnL();
		    	
		    	ClientView.getInstance().updateOrder();
		    	
		    	MsgDialog.getInstance().showSuccess("Your send order is complete!");
		    	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pause.play();
		
	}

	@SuppressWarnings("unchecked")
	private void handleReceivedAsset(Object information) { //When the client receives an asset from another user thanks to the send feature.
		
		Vector<Object> packet = (Vector<Object>) information;
		Transaction transaction = (Transaction) packet.elementAt(0);
		Map<String, Double> amountOwned = (Map<String, Double>) packet.elementAt(1);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(e -> {
		    try {
				handleAmountOwned(amountOwned);
				ClientLogic.getInstance().setReceivedTransaction(transaction.getAmount(), transaction.getTotal(), transaction.getCrypto(), transaction.getBuy(), transaction.getDate(), transaction.getRecipient(), transaction.getUsername());
				ClientLogic.getInstance().addTransaction();
				
		    	ClientView.getInstance().updateBalance();
		    	ClientLogic.getInstance().updatePortfolioValue();
		    	ClientView.getInstance().updatePieChart();
		    	ClientLogic.getInstance().updatePnL();
		    	
		    	ClientView.getInstance().updateOrder();
		    	
		    	MsgDialog.getInstance().showReceived(transaction);
		    	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pause.play();
	}

	private void handleUserInexistent() throws IOException {
		MsgDialog.getInstance().showError("Account holder username is inexistent.");
	}

	private void handleWithdrawFailed() throws IOException {
		MsgDialog.getInstance().showError("I'm sorry! Withdraw failed. Please try again.");
	}

	private void handleWithdrawResponse(Object information) throws IOException { //Bank Withdraw successfull
		Double balance = (Double) information;
		
		PauseTransition pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(e -> {
		    try {
		    	handleBalanceResponse(balance);
		    	SendWithdrawDialog.getInstance().updateBalance();
		    	SendWithdrawDialog.getInstance().resetInfo();
		    	ClientView.getInstance().updateBalance();
		    	ClientView.getInstance().updatePieChart();
		    	ClientLogic.getInstance().addTransaction();
		    	ClientLogic.getInstance().updatePortfolioValue();
		    	DepositDialog.getInstance().updateBalance();
		    	
		    	SendWithdrawDialog.getInstance().goBack();
		    	
		    	MsgDialog.getInstance().showWithdrawSuccessful();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pause.play();
	}

	private void handleTransactionsFailed() {
		System.out.println("[ERROR] Transactions request failed");
	}

	private void handleTransactionsResponse(Object information) throws IOException { //Users transactions response from server
		@SuppressWarnings("unchecked")
		Vector<Transaction> transactions = (Vector<Transaction>) information;
		ClientLogic.getInstance().setTransactions(transactions);		
	}

	private void handleDepositFailed() {
		System.out.println("[ERROR] Deposit failed");
	}

	private void handleDeposit(Object information) throws IOException { //Deposit USD successful
		Double balance = (Double) information;
		
		PauseTransition pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(e -> {
		    try {
		    	handleBalanceResponse(balance);
		    	
		    	DepositDialog.getInstance().resetCard();
		    	DepositDialog.getInstance().updateBalance();
		    	ClientView.getInstance().updateBalance();
		    	ClientView.getInstance().updatePieChart();
		    	ClientLogic.getInstance().addTransaction();
		    	DepositDialog.getInstance().goBack();
		    	
		    	MsgDialog.getInstance().showTransactionSuccessful();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pause.play();
	}

	private void handleAmountOwnedRequestFailed() {
		System.out.println("[ERROR] Amount owned request failed");
	}

	private void handleAmountOwned(Object information){ //Amounts owned by the user regarding all assets
		@SuppressWarnings("unchecked")
		Map<String, Double> amountOwned = (Map<String, Double>) information;
		ClientLogic.getInstance().setAmountOwned(amountOwned);
		ClientLogic.getInstance().setAmountOwnedReady(true);
		ClientLogic.getInstance().updateAmountOwnedPortfolio();
		ClientLogic.getInstance().updatePortfolioValue();
	}

	private void handleBalanceRequestFailed() {
		System.out.println("[ERROR] Balance request failed!");
	}

	private void handleBalanceResponse(Object information) throws IOException { //The balance of the user
		Double balance = (Double) information;
		ClientLogic.getInstance().setBalance(balance);
		ClientLogic.getInstance().setBalanceReady(true);
		ClientView.getInstance().getAssetController(Protocol.USD).setBalance();
		ClientLogic.getInstance().updatePortfolioValue();
	}

	private void handleFailedTransaction() throws IOException {
		MsgDialog.getInstance().showError("An error occurred. Please retry");
	}

	public void handleSuccessfulTransaction(Object information) throws IOException { //Transaction was successful
		@SuppressWarnings("unchecked")
		Vector<Object> packet = (Vector<Object>) information;
		
		PauseTransition pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(e -> {
		    try {
		    	handleBalanceResponse(packet.elementAt(0));
				handleAmountOwned(packet.elementAt(1));
		    	
				ClientLogic.getInstance().updatePortfolioValue();
		    	ClientView.getInstance().updateBalance();
		    	ClientView.getInstance().updatePieChart();
		    	ClientLogic.getInstance().addTransaction();
		    	ClientLogic.getInstance().updatePnL();
		    	DepositDialog.getInstance().updateBalance();
		    	
		    	MsgDialog.getInstance().showTransactionSuccessful();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pause.play();
	}

}

	
