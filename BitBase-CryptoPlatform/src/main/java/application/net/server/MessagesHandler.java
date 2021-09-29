package application.net.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import application.net.common.DepositWithdraw;
import application.net.common.Message;
import application.net.common.Protocol;
import application.net.common.Stats;
import application.net.common.Transaction;
import application.net.common.User;
import javafx.util.Pair;

public class MessagesHandler implements Runnable { //Handles all the requests received from the clients 

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String username = "";
	private boolean logged = false;

	public MessagesHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
	}

	private void closeStreams() throws IOException {
		if (out != null)
			out.close();
		out = null;
		if (in != null)
			in.close();
		in = null;
		if (socket != null)
			socket.close();
		socket = null;
	}

	@Override
	public void run() {		
		try {
			while(!logged) {
				this.in = new ObjectInputStream(socket.getInputStream());
				String input = (String) in.readObject();			
				if (input.equals(Protocol.LOGIN)) {
					User user = (User) in.readObject();	
					if (!DatabaseHandler.getInstance().checkUser(user)) {
						sendMessage(Protocol.AUTHENTICATION_ERROR);
						closeStreams();
						return;
					}
					
					logged = true;
					username = user.getUsername();
					
					Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " Logged in.");
					
					if(!UsersHandler.insertUser(username, this)) {
						sendMessage(Protocol.USER_LOGGED_ERROR);
						closeStreams();
						return;
					}
				} else if (input.equals(Protocol.REGISTRATION)) {
					User user = (User) in.readObject();
					if (DatabaseHandler.getInstance().existsUser(user)) {
						sendMessage(Protocol.USER_EXISTS_ERROR);
						closeStreams();
						return;
					} else {
						if (!DatabaseHandler.getInstance().insertUser(user)) {
							sendMessage(Protocol.ERROR);
							
							Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " Error while signing up.");
							
							closeStreams();
							return;
						}
					}
					Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " +  user.getUsername() + " signed up.");
				} else {
					sendMessage(Protocol.ERROR);
					closeStreams();
					
					return;
				}
				if(logged) {
					if(Server.statsReady())
						sendMessage(Protocol.OK);
					else
						sendMessage(Protocol.CRYPTO_STATS_REQUEST_FAILED);
				}
				else //Registration
					sendMessage(Protocol.OK);
			}
			
			//User is logged
			UsersHandler.addUserStream(username, out);
			
			while(!Thread.currentThread().isInterrupted()) {
				String request = (String) in.readObject();
				System.out.println("[SERVER] " + username + " performed: " + request);
				Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " performed " + request);
				switch(request) {
					case Protocol.TRANSACTION:{
						handleTransaction();
						break;
					}
					case Protocol.BALANCE_REQUEST:{
						handleBalanceRequest();
						break;
					}
					case Protocol.AMOUNT_OWNED_REQUEST:{
						handleAmountOwnedRequest();
						break;
					}
					case Protocol.DEPOSIT_REQUEST:{
						handleDepositRequest();
						break;
					}
					case Protocol.TRANSACTIONS_REQUEST:{
						handleTransactionsRequest();
						break;
					}
					case Protocol.WITHDRAW_REQUEST:{
						handleWithdrawRequest();
						break;
					}
					case Protocol.SEND_REQUEST:{
						handleSendRequest();
						break;
					}
					case Protocol.UPDATE_USERNAME_REQUEST:{
						handleUpdateUsernameRequest();
						break;
					}
					case Protocol.UPDATE_USERNAME_PASSWORD_REQUEST:{
						handleUpdateUsernameAndPasswordRequest();
						break;
					}
					case Protocol.UPDATE_PASSWORD_REQUEST:{
						handleUpdatePasswordRequest();
						break;
					}
					case Protocol.CRYPTO_STATS_REQUEST:{
						handleCryptoStatsRequest();
						break;
					}
				}
			}			
		} catch (Exception e) {
			if(!username.equals("")) {
				UsersHandler.removeUser(username);
				System.out.println("[SERVER] " + username + " logged out or lost connection.");
				try {
					Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " Logged out.");
				} catch (IOException e1) {
					e1.printStackTrace();	
				}
			}
			else {
				sendMessage(Protocol.ERROR);
			}
			try {
				closeStreams();
			} catch (IOException e1) {
				//nothing otherwise it would show that client interrupted connection which is known
			}
			return;
		}
	}
	
	public static void sendUpdatedStats() throws IOException { //Sends updates statistics
		Vector<String> onlineUsers = UsersHandler.allUsers();
		
		Map<String, Stats> cryptoStats = Server.getCryptoStats();
		
		for(int i=0; i<onlineUsers.size(); i++) {
			ObjectOutputStream recipientStream = UsersHandler.getStream(onlineUsers.elementAt(i)); //Now send notification to recipient if he is online
			
			recipientStream.writeObject(new Message(Protocol.CRYPTO_STATS_RESPONSE, cryptoStats));
			recipientStream.flush();	
		}
	}
	
	private void handleCryptoStatsRequest() throws IOException { //Sends the asset stats when a user logs in. Checks if the stats are ready before sending. 
		if(Server.statsReady()) {
			Map<String, Stats> cryptoStats = Server.getCryptoStats();
			sendMessage(new Message(Protocol.CRYPTO_STATS_RESPONSE, cryptoStats));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " crypto stats request successfull");
		}
		else {
			sendMessage(new Message(Protocol.CRYPTO_STATS_REQUEST_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.CRYPTO_STATS_REQUEST_FAILED);
		}
	}

	private void handleUpdatePasswordRequest() throws ClassNotFoundException, IOException, SQLException { //Handles a password update request
		@SuppressWarnings("unchecked")
		Vector<String> info = (Vector<String>) in.readObject();
		String username = info.elementAt(0);
		String oldPassword = info.elementAt(1);
		String newPassword = info.elementAt(2);
		
		if(!DatabaseHandler.getInstance().checkUser(new User(username, oldPassword, null, null))) {
			sendMessage(new Message(Protocol.PASSWORD_INVALID, null));
			return;
		}
		
		if(!DatabaseHandler.getInstance().changePassword(username, newPassword)) {
			sendMessage(new Message(Protocol.PASSWORD_CHANGE_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.PASSWORD_CHANGE_FAILED);
			return;
		}
		
		sendMessage(new Message(Protocol.UPDATE_PASSWORD_RESPONSE, null));
		Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " changed password");
	}

	private void handleUpdateUsernameAndPasswordRequest() throws ClassNotFoundException, IOException, SQLException { //Handles username and password update request
		@SuppressWarnings("unchecked")
		Vector<String> info = (Vector<String>) in.readObject();
		String oldUsername =  info.elementAt(0);
		String newUsername =  info.elementAt(1);
		String oldPassword =  info.elementAt(2);
		String newPassword =  info.elementAt(3);
		
		if(DatabaseHandler.getInstance().existsUser(new User(newUsername, null, null, null))) {
			sendMessage(new Message(Protocol.USER_EXISTS_ERROR, null));
			return;
		}
		
		if(!DatabaseHandler.getInstance().checkUser(new User(oldUsername, oldPassword, null, null))) {
			sendMessage(new Message(Protocol.PASSWORD_INVALID, null));
			return;
		}
		
		//change password
		if(!DatabaseHandler.getInstance().changePassword(oldUsername, newPassword)) {
			sendMessage(new Message(Protocol.PASSWORD_CHANGE_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.PASSWORD_CHANGE_FAILED);
			return;
		}
		
		//change username
		if(!DatabaseHandler.getInstance().updateUsername(newUsername, oldUsername)) {
			sendMessage(new Message(Protocol.UPDATE_USERNAME_FAILED, null)); //update was unsuccessful
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.UPDATE_USERNAME_FAILED);
			return;
		}
		
		sendMessage(new Message(Protocol.UPDATE_USERNAME_PASSWORD_RESPONSE, newUsername));
		Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " changed username to " + newUsername + " and changed password");
		username = newUsername;
		UsersHandler.removeUser(oldUsername);
		UsersHandler.addUserStream(username, out);
	}

	private void handleUpdateUsernameRequest() throws ClassNotFoundException, IOException, SQLException { //Handles a username change request
		@SuppressWarnings("unchecked")
		Pair<String, String> updateUsername = (Pair<String, String>) in.readObject();
		
		if(DatabaseHandler.getInstance().existsUser(new User(updateUsername.getKey(), null, null, null))) {
			sendMessage(new Message(Protocol.USER_EXISTS_ERROR, null));
			return;
		}
		
		if(!DatabaseHandler.getInstance().updateUsername(updateUsername.getKey(), updateUsername.getValue())) {
			sendMessage(new Message(Protocol.UPDATE_USERNAME_FAILED, null)); //update was unsuccessful
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.UPDATE_USERNAME_FAILED);
			
			return;
		}

		sendMessage(new Message(Protocol.UPDATE_USERNAME_RESPONSE, updateUsername.getKey()));
			
		Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " changed username to " + updateUsername.getKey());
		username = updateUsername.getKey();
		UsersHandler.removeUser(updateUsername.getValue());
		UsersHandler.addUserStream(username, out);
	}

	private void handleSendRequest() throws ClassNotFoundException, IOException, SQLException { //Handles a send asset request from a client to another client
		Transaction transaction = (Transaction) in.readObject();
		if(!DatabaseHandler.getInstance().existsUser(new User(transaction.getRecipient(), null, null, null))) {
			sendMessage(new Message(Protocol.USER_INEXISTENT, null));
			return;
		}

		transaction.setDate(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString());
		
		if(!DatabaseHandler.getInstance().send(transaction)) { //Reduce amount of asset sent and add it to the recepient
			sendMessage(new Message(Protocol.SEND_FAILED, null)); //Sent was unsuccessful
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.SEND_FAILED);
		}
		else { //Send was successful
			Map<String, Double> amountOwned = DatabaseHandler.getInstance().retrieveAmountOwned(transaction.getUsername()); //Update amount owned of sender
			sendMessage(new Message(Protocol.SEND_RESPONSE, amountOwned)); 
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " send request successful");
			
			ObjectOutputStream recipientStream = UsersHandler.getStream(transaction.getRecipient()); //Now send notification to recipient if he is online
			if(recipientStream==null) {
				return;
			}
			
			Map<String, Double> amountOwnedRecipient = DatabaseHandler.getInstance().retrieveAmountOwned(transaction.getRecipient()); //Update recipient amount owned
			Vector<Object> packet = new Vector<Object>();
			packet.add(transaction); //Send "SEND" transaction for info on who and what was sent
			packet.add(amountOwnedRecipient); //Send amount owned to update portfolio
			recipientStream.writeObject(new Message(Protocol.RECEIVED_ASSET, packet));
			recipientStream.flush();	
		}
		
	}

	private void handleWithdrawRequest() throws ClassNotFoundException, IOException, SQLException { //Handles a withdraw request of FIAT money from Client to a bank account
		DepositWithdraw withdraw = (DepositWithdraw) in.readObject();		
		if(!DatabaseHandler.getInstance().deposit(withdraw)) {
			sendMessage(new Message(Protocol.WITHDRAW_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + Protocol.WITHDRAW_FAILED);
		}
		else {
			Double balance = DatabaseHandler.getInstance().retrieveBalance(withdraw.getUsername());
			sendMessage(new Message(Protocol.WITHDRAW_RESPONSE, balance));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " withdraw request successful");
		}
	}

	private void handleTransactionsRequest() throws ClassNotFoundException, IOException { //Handles the request to receive all transactions of the user
		String username = (String) in.readObject();
		Vector<Transaction> transactions = DatabaseHandler.getInstance().retrieveTransactions(username);
		if(transactions == null) {
			sendMessage(new Message(Protocol.TRANSACTIONS_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.TRANSACTIONS_FAILED);
		}
		else {
			sendMessage(new Message(Protocol.TRANSACTIONS_RESPONSE, transactions));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " user transactions request successful");
		}
			
		
	}

	private void handleDepositRequest() throws ClassNotFoundException, IOException, SQLException { //Handles a deposit of FIAT money
		DepositWithdraw deposit = (DepositWithdraw) in.readObject();
		if(!DatabaseHandler.getInstance().deposit(deposit)) {
			sendMessage(new Message(Protocol.DEPOSIT_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.DEPOSIT_FAILED);
		}
		else {
			Double balance = DatabaseHandler.getInstance().retrieveBalance(deposit.getUsername());
			sendMessage(new Message(Protocol.DEPOSIT_RESPONSE, balance));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " deposited from card");
		}
	}

	private void handleAmountOwnedRequest() throws ClassNotFoundException, IOException { //Handles the request to receive all amounts of assets owned
		String username = (String) in.readObject();
		Map<String, Double> amountOwned = DatabaseHandler.getInstance().retrieveAmountOwned(username);
		if(amountOwned == null) {
			sendMessage(new Message(Protocol.AMOUNT_OWNED_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.AMOUNT_OWNED_FAILED);
		}
		else {
			sendMessage(new Message(Protocol.AMOUNT_OWNED_RESPONSE, amountOwned));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " amount owned request successful");
		}
	}

	private void handleBalanceRequest() throws ClassNotFoundException, IOException, SQLException { //Handles the FIAT money balance request
		String username = (String) in.readObject();
		Double balance = DatabaseHandler.getInstance().retrieveBalance(username);
		if(balance == null) {
			sendMessage(new Message(Protocol.BALANCE_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.BALANCE_FAILED);
		}
		else {
			sendMessage(new Message(Protocol.BALANCE_RESPONSE, balance));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " balance request successful");
		}
	}

	private void handleTransaction() throws ClassNotFoundException, IOException, SQLException { //Handles a transaction performed by the user
		Transaction transaction = (Transaction) in.readObject();
		transaction.setDate(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString()); //Always set the server's date and time not client's
		
		if (!DatabaseHandler.getInstance().performTransaction(transaction)) {
			sendMessage(new Message(Protocol.TRANSACTION_FAILED, null));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " " + Protocol.TRANSACTION_FAILED);
		}
		else {
			//sendMessage(new Message(Protocol.TRANSACTION_SUCCESSFUL, null));
			Double balance = DatabaseHandler.getInstance().retrieveBalance(transaction.getUsername());
			Map<String, Double> amountOwned = DatabaseHandler.getInstance().retrieveAmountOwned(transaction.getUsername());
			Vector<Object> packet = new Vector<Object>();
			packet.add(balance);
			packet.add(amountOwned);
			sendMessage(new Message(Protocol.TRANSACTION_SUCCESSFUL, packet));
			
			Log.getInstance().addEvent(java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString() + " " + username + " transaction request successful.");
		}
	}
	
	

	public void sendMessage(Object message) {
		if (out == null)
			return;
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			if(!username.equals("")) {
				UsersHandler.removeUser(username);
			}
		}
	}

	
}
