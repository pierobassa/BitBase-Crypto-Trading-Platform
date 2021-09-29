package application.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import application.MsgDialog;
import application.net.common.DepositWithdraw;
import application.net.common.Message;
import application.net.common.Protocol;
import application.net.common.Transaction;
import application.net.common.User;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;

public class ClientService extends Service<Message>{ //Handles the communications from the client to the Server
	
	private static ClientService instance = null;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private ClientService() throws IOException {
		try {
			socket = new Socket("localhost", 8000);
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			out = null;
			MsgDialog.getInstance().showError("Something went wrong connecting to the server.");
		}
	}
	
	public static ClientService getInstance() throws IOException {
		if(instance == null)
			instance = new ClientService();
		return instance;
	}
	
	
	public void resetClient() {
		try {
			if(in != null)
				in.close();
			
			if(out != null)
				out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		instance = null;
		out = null;
		in = null;
		socket = null;
	}	
	
	public String authenticationLogin(String username, String password) {
		sendMessagePrivate(Protocol.LOGIN);		
		sendMessagePrivate(new User(username, password, null, null));
		try {
			in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			return res;			
		} catch (Exception e) {
			out = null;
			return Protocol.ERROR;
		}
	}
	
	public String authenticationSignup(String username, String password, String name, String surname) {
		sendMessagePrivate(Protocol.REGISTRATION);		
		sendMessagePrivate(new User(username, password, name, surname));
		try {
			in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			return res;			
		} catch (Exception e) {
			out = null;
			return Protocol.ERROR;
		}
	}
	
	public void requestBalance(String username) {
		sendMessagePrivate(Protocol.BALANCE_REQUEST);
		sendMessagePrivate(username);
	}
	
	public void requestAmountOwned(String username) { //Requests the amounts owned of each asset
		sendMessagePrivate(Protocol.AMOUNT_OWNED_REQUEST);
		sendMessagePrivate(username);
	}
	
	public void transaction(double amount, double total, String crypto, String buy, String username, String date) { //When a user performs a transaction it must be communicated to the server
		sendMessagePrivate(Protocol.TRANSACTION);
		sendMessagePrivate(new Transaction(amount, total, crypto, buy, username, date, null));
	}
	
	public void depositRequest(DepositWithdraw deposit) { 
		sendMessagePrivate(Protocol.DEPOSIT_REQUEST);
		sendMessagePrivate(deposit);
	}
	
	public void requestTransactions(String username) { //Requests all the transactions of the user
		sendMessagePrivate(Protocol.TRANSACTIONS_REQUEST);
		sendMessagePrivate(username);		
	}
	
	public void withdrawRequest(DepositWithdraw withdraw) { 
		sendMessagePrivate(Protocol.WITHDRAW_REQUEST);
		sendMessagePrivate(withdraw);
	}
	
	public void sendRequest(Double amount, Double total, String asset, String username, String recipient) { //Requests a send operation of an asset to another user
		sendMessagePrivate(Protocol.SEND_REQUEST);
		sendMessagePrivate(new Transaction(amount, total, asset, Protocol.SEND, username, null, recipient));
	}
	
	public void updateUsernameRequest(String newUsername, String username) { 
		sendMessagePrivate(Protocol.UPDATE_USERNAME_REQUEST);
		sendMessagePrivate(new Pair<String, String>(newUsername, username));
	}
	
	public void updateUsernameAndPasswordRequest(String oldUsername, String newUsername, String oldPassword, String newPassword) {
		sendMessagePrivate(Protocol.UPDATE_USERNAME_PASSWORD_REQUEST);
		Vector<String> mess = new Vector<String>();
		mess.add(oldUsername);
		mess.add(newUsername);
		mess.add(oldPassword);
		mess.add(newPassword);
		sendMessagePrivate(mess);
	}
	
	public void updatePassword(String username, String oldPassword, String newPassword) {
		sendMessagePrivate(Protocol.UPDATE_PASSWORD_REQUEST);
		Vector<String> mess = new Vector<String>();
		mess.add(username);
		mess.add(oldPassword);
		mess.add(newPassword);
		sendMessagePrivate(mess);
	}
	
	public void requestCryptoStats() { //Requests the statistics of all assets
		sendMessagePrivate(Protocol.CRYPTO_STATS_REQUEST);
	}
	
	private boolean sendMessagePrivate(Object message) {
		if(out == null)
			return false;
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			out = null;			
			return false;
		}
		return true;
	}
	
	public boolean sendMessage(String message) {
		return sendMessagePrivate(message);
	}
	
	@Override
	protected Task<Message> createTask() {
		return new Task<Message>() {
			@Override
			protected Message call() throws Exception {
				Message mess = (Message) in.readObject(); //Message received from the server to the user
				try {
					return mess; //returns the message handled by the ClientOnSucceededController class
				} catch (Exception e) {
					return null;
				}	
			}
		};
	}
}
