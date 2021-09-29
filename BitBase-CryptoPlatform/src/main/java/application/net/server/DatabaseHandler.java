package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.springframework.security.crypto.bcrypt.BCrypt;
import application.net.common.DepositWithdraw;
import application.net.common.Protocol;
import application.net.common.SupportedAssets;
import application.net.common.Transaction;
import application.net.common.User;

public class DatabaseHandler { //Handles all operations (DML's and DQL'S) with the server database

	private static DatabaseHandler instance = null;
	private Connection con = null;
	
	private DatabaseHandler() {
		try {
			con = DriverManager.getConnection("jdbc:sqlite:Database.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DatabaseHandler getInstance() {
		if(instance == null)
			instance = new DatabaseHandler();
		return instance;
	}

	
	public synchronized boolean insertUser(User user) throws SQLException {
		if(con == null || con.isClosed() || user == null)
			return false;
		
		if(existsUser(user))
			return false;
		
		PreparedStatement p = con.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?);");
		p.setString(1, user.getUsername());
		p.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		p.setString(3, user.getName());
		p.setString(4, user.getSurname());
		p.setDouble(5, 0.0);
		p.executeUpdate();
		p.close();
		
		String query = "INSERT INTO owns VALUES(?, ?, ?);";
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				p = con.prepareStatement(query);
				p.setString(1, user.getUsername());
				p.setString(2, key);
				p.setDouble(3, 0.0);
				p.executeUpdate();
				p.close();
			}
		}
		
		return true;
	}
	
	public synchronized boolean existsUser(User user) throws SQLException {
		if(con == null || con.isClosed() || user == null)
			return false;
		
		String query = "SELECT * FROM users WHERE username=?;";
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, user.getUsername());
		ResultSet rs = p.executeQuery();
		boolean result = rs.next();
		p.close();
		return result;		
	}
	
	public synchronized boolean checkUser(User user) throws SQLException {
		if(con == null || con.isClosed() || user == null)
			return false;
		
		String query = "SELECT * FROM users WHERE username=?;";
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, user.getUsername());
		ResultSet rs = p.executeQuery();
		boolean result = false;
		if(rs.next()) {
			String password = rs.getString("password");
			result = BCrypt.checkpw(user.getPassword(), password);			
		}
		p.close();
		return result;
	}

	public synchronized boolean performTransaction(Transaction transaction) {
		try {
			if(con == null || con.isClosed() || transaction == null)
				return false;
		
			String crypto = transaction.getCrypto();
			Double amount = transaction.getAmount();
			Double total = transaction.getTotal();
			String buy = transaction.getBuy();
			String username = transaction.getUsername();
			String date = transaction.getDate();
			
			String queryOwns = null;
			String queryBalance = null;
			
			if(buy.equals(Protocol.BUY)) { //IF BUY ORDER
				queryOwns = "UPDATE owns SET quantity = quantity + ? WHERE crypto_name=? AND username=?;";
				queryBalance = "UPDATE users SET balance = balance - ? WHERE username=?;";
			}
			else if(buy.equals(Protocol.SELL)){ //IF SELL ORDER
				queryOwns = "UPDATE owns SET quantity = quantity - ? WHERE crypto_name=? AND username=?;";
				queryBalance = "UPDATE users SET balance = balance + ? WHERE username=?;";
			}

			PreparedStatement p = con.prepareStatement(queryOwns);
			p.setDouble(1, amount);
			p.setString(2, crypto);
			p.setString(3, username);
			
			p.executeUpdate();
			p.close();
			
			p = con.prepareStatement(queryBalance);
			p.setDouble(1, total);
			p.setString(2, username);
		
			p.executeUpdate();
			p.close();
			
			String addTransaction = "INSERT INTO transactions VALUES(null, ?, ?, ?, ?, ?, ?, null)";
			p = con.prepareStatement(addTransaction);
			p.setString(1, username);
			p.setString(2, crypto);
			p.setDouble(3, amount);
			p.setDouble(4, total);
			if(buy.equals(Protocol.BUY))
				p.setString(5, Protocol.BUY); //1 = buy order
			else if(buy.equals(Protocol.SELL))
				p.setString(5, Protocol.SELL); //0 = sell order
			p.setString(6, date);
			
			p.executeUpdate();
			p.close();
			
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	public synchronized Double retrieveBalance(String username) throws SQLException {
		if(con == null || con.isClosed() || username == null)
			return null;
		try {
			String query = "SELECT balance FROM users WHERE username=?;";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, username);
			ResultSet rs = p.executeQuery();
			Double balance = null;
			if(rs.next()) {
				balance = rs.getDouble("balance");
			}
			p.close();
			return balance;
		}
		catch(Exception e) {
			return null;
		}

	}

	public synchronized Map<String, Double> retrieveAmountOwned(String username) {
		try {
			if(con == null || con.isClosed() || username == null)
				return null;
			
			Map<String, Double> amountOwned = new HashMap<String, Double> ();
			
			String query = "SELECT * FROM owns WHERE username=?;";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, username);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				amountOwned.put(rs.getString("crypto_name"), rs.getDouble("quantity"));
			}
			
			return amountOwned;
		} catch (SQLException e) {
			return null;
		}
	}

	public synchronized boolean deposit(DepositWithdraw deposit) {
		try {
			if(con == null || con.isClosed() || deposit == null)
				return false;
			
			String username = deposit.getUsername();
			Double amount = deposit.getDepositAmount();
			
			String query;
			if(deposit.getDeposit())
				query = "UPDATE users SET balance = balance + ? WHERE username=?;";
			else
				query = "UPDATE users SET balance = balance - ? WHERE username=?;";
			
			PreparedStatement p = con.prepareStatement(query);
			p.setDouble(1, amount);
			p.setString(2, username);
			
			p.executeUpdate();
			p.close();
			
			String addTransaction = "INSERT INTO transactions VALUES(null, ?, null, ?, ?, ?, ?, null)";
			p = con.prepareStatement(addTransaction);
			p.setString(1, username);
			p.setDouble(2, amount);
			p.setDouble(3, amount);
			if(deposit.getDeposit())
				p.setString(4, Protocol.DEPOSIT); //2 = deposit transaction
			else
				p.setString(4, Protocol.WITHDRAW); //3 = withdraw transaction
			p.setString(5, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString());
			
			p.executeUpdate();
			p.close();
			
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	public synchronized Vector<Transaction> retrieveTransactions(String username) {
		try {
			if(con == null || con.isClosed() || username == null)
				return null;
			
			Vector<Transaction> transactions = new Vector<Transaction>();
			
			String query = "SELECT * FROM transactions WHERE username=? OR recipient=?;";
			
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, username);
			p.setString(2, username);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				transactions.add(new Transaction(rs.getDouble(4), rs.getDouble(5), rs.getString("crypto"), rs.getString("buy"), rs.getString("username"), rs.getString("date"), rs.getString("recipient")));
			}
			
			return transactions;
		}catch (SQLException e) {
			return null;
		}
	}

	public synchronized boolean send(Transaction transaction) {
		try {
			if(con == null || con.isClosed() || transaction == null)
				return false;
			
			String sender = transaction.getUsername();
			String recipient = transaction.getRecipient();
			Double amount = transaction.getAmount();
			Double total = transaction.getTotal();
			String crypto = transaction.getCrypto();
			
			String query;
			query = "UPDATE owns SET quantity = quantity - ? WHERE username=? AND crypto_name=?;";
			
			PreparedStatement p = con.prepareStatement(query);
			
			p.setDouble(1, amount);
			p.setString(2, sender);
			p.setString(3, crypto);
			
			p.executeUpdate();
			p.close();
			
			query = "UPDATE owns SET quantity = quantity + ? WHERE username=? AND crypto_name=?;";
			p = con.prepareStatement(query);
			
			p.setDouble(1, amount);
			p.setString(2, recipient);
			p.setString(3, crypto);
			
			p.executeUpdate();
			p.close();
			
			String addTransaction = "INSERT INTO transactions VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
			p = con.prepareStatement(addTransaction);
			p.setString(1, sender);
			p.setString(2, crypto);
			p.setDouble(3, amount);
			p.setDouble(4, total);
			p.setString(5, Protocol.SEND);
			p.setString(6, transaction.getDate());
			p.setString(7, recipient);
			
			p.executeUpdate();
			p.close();
			
			return true;			
		}
		catch(SQLException e) {
			return false;
		}
	}
 
	public synchronized boolean updateUsername(String newUsername, String oldUsername) {
		try {
			if(con == null || con.isClosed() || newUsername == null || oldUsername == null)
				return false;
			
			String query;
			query = "UPDATE users SET username = ? WHERE username = ?;";
			
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, newUsername);
			p.setString(2, oldUsername);
			
			p.executeUpdate();
			p.close();
			
			query = "UPDATE transactions SET username = ? WHERE username = ?;";
			
			p = con.prepareStatement(query);
			
			p.setString(1, newUsername);
			p.setString(2, oldUsername);
			
			p.executeUpdate();
			p.close();
			
			query = "UPDATE transactions SET recipient = ? WHERE recipient = ?;";
			
			p = con.prepareStatement(query);
			
			p.setString(1, newUsername);
			p.setString(2, oldUsername);
			
			p.executeUpdate();
			p.close();
			

			query = "UPDATE owns SET username = ? WHERE username = ?;";
			
			p = con.prepareStatement(query);
			
			p.setString(1, newUsername);
			p.setString(2, oldUsername);
			
			p.executeUpdate();
			p.close();
			
			return true;			
		}
		catch(SQLException e) {
			return false;
		}
	}

	public synchronized boolean changePassword(String username, String newPassword) {
		try {
			if(con == null || con.isClosed() || username == null || newPassword == null)
				return false;
			
			String query;
			query = "UPDATE users SET password = ? WHERE username = ?;";
			
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
			p.setString(2, username);
			
			p.executeUpdate();
			p.close();
			
			return true;			
		}
		catch(SQLException e) {
			return false;
		}
	}
}
