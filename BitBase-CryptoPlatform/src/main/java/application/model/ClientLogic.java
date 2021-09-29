package application.model;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import application.ClientView;
import application.DepositDialog;
import application.MyAccountDialog;
import application.SceneHandler;
import application.SendWithdrawDialog;
import application.data.tasks.Update30DayPrices;
import application.data.tasks.UpdateCurrentPrices;
import application.data.tasks.UpdateNews;
import application.net.client.ClientService;
import application.net.common.DepositWithdraw;
import application.net.common.Protocol;
import application.net.common.SupportedAssets;
import application.net.common.Transaction;


public class ClientLogic { //Class that is responsible of the client application logic
	private static ClientLogic instance = null;
	private String username;
	
	private Double balance; //Users FIAT balance
	private Map<String, Double> amountOwned; //Map that will contain the amounts owned of each asset
	private Vector<Transaction> transactions; //Container of all the transactions done by the user
	private Transaction temporaryTransaction;
	private Map<String, Vector<Transaction>> assetTransactions;
	
	//Flags that are needed to be true for the application to be ready.
	private boolean prices30DayReady = false;
	private boolean pricesReady = false;
	private boolean newsReady = false;
	private boolean statsReady = false;
	private boolean amountOwnedReady = false;
	private boolean balanceReady = false;
	private boolean transactionsReady = false;
	private static Map<String, Boolean> statsReadyMap;
	private static Map<String, Boolean> prices30DaysReadyMap;
	private static Map<String, Boolean> pricesReadyMap;
	
	public ClientLogic() {
		amountOwned = new HashMap<String, Double>();
		prices30DaysReadyMap = new HashMap<String, Boolean>();
		pricesReadyMap = new HashMap<String, Boolean>();
		assetTransactions = new HashMap<String, Vector<Transaction>>();
		
		statsReadyMap = new HashMap<String, Boolean>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				statsReadyMap.put(key, false);
				prices30DaysReadyMap.put(key, false);
				pricesReadyMap.put(key, false);
				assetTransactions.put(key, new Vector<Transaction>());
			}
		}
	}
	
	public static ClientLogic getInstance() {
		if(instance == null)
			instance = new ClientLogic();
		
		return instance;
	}
	
	public void resetClientLogic() throws IOException { //Resets the client logic
		SceneHandler.getInstance().removeSceneBlur(); //If a dialog is open
		SceneHandler.getInstance().setLoginScene();
		ClientService.getInstance().resetClient();
		Prices.getInstance().reset();
		UpdateCurrentPrices.stop();
		UpdateNews.stop();
		Update30DayPrices.stop();
		ClientView.getInstance().reset();
		
		balance = null;
		amountOwned.clear();
		
		newsReady = false;
		prices30DayReady = false;
		pricesReady = false;
		statsReady = false;
		amountOwnedReady = false;
		balanceReady = false;
		transactionsReady = false;		
		
		//If a dialog, besides the MessageDialog, is open, it needs to be closed if the client is reseted.
		SendWithdrawDialog.close();
		DepositDialog.close();
		MyAccountDialog.close();
		
		for(var key : prices30DaysReadyMap.keySet())
			prices30DaysReadyMap.put(key, false);
		for(var key : pricesReadyMap.keySet())
			pricesReadyMap.put(key, false);
		for(var key : statsReadyMap.keySet())
			statsReadyMap.put(key, false);
		
		transactions = null;
		for(var key : assetTransactions.keySet())
			assetTransactions.get(key).clear();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setAmountOwned(Map<String, Double> amountOwned) {
		this.amountOwned = amountOwned;
	}
	
	public void setAmountOwnedReady(boolean ready) {
		amountOwnedReady = ready;
		
		System.out.println("AMOUNT OWNED READY");
	}
	
	public void setBalanceReady(boolean ready) {
		balanceReady = ready;
		
		System.out.println("BALANCE READY");
	}
	
	public Double getAmountOwned(String crypto) {
		return amountOwned.get(crypto);
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public boolean getPrices30DayReady() {
		return prices30DayReady;
	}
	
	//START FUNCTIONS THAT CALL CLIENTSERVICE TO COMMUNICATE WITH THE SERVER
	public void transaction(Double amount, Double price, String crypto, String buy, String date) throws IOException {
		ClientService.getInstance().transaction(amount, price, crypto, buy, username, date);
	}

	public void balance() throws IOException {
		ClientService.getInstance().requestBalance(username);
		
	}
	
	public void amountOwned() throws IOException {
		ClientService.getInstance().requestAmountOwned(username);
	}
	
	public void deposit(Double depositAmount) throws IOException {
		ClientService.getInstance().depositRequest(new DepositWithdraw(username, depositAmount, true));
	}
	
	public void transactions() throws IOException {
		ClientService.getInstance().requestTransactions(username);
	}
	
	public void withdraw(Double withdrawAmount) throws IOException {
		ClientService.getInstance().withdrawRequest(new DepositWithdraw(username, withdrawAmount, false));
	}
	
	public void send(Double amount, Double total, String asset, String recipient) throws IOException {
		ClientService.getInstance().sendRequest(amount, total, asset, username, recipient);
	}
	
	public void updateUsername(String newUsername) throws IOException {
		ClientService.getInstance().updateUsernameRequest(newUsername, username);
	}
	
	public void updateUsernameAndPassword(String newUsername, String oldPassword, String newPassword) throws IOException {
		ClientService.getInstance().updateUsernameAndPasswordRequest(username, newUsername, oldPassword, newPassword);
	}
	

	public void updatePassword(String oldPassword, String newPassword) throws IOException {
		ClientService.getInstance().updatePassword(username, oldPassword, newPassword);
	}
	
	public void requestCryptoStats() throws IOException {
		ClientService.getInstance().requestCryptoStats();
		
	}
	//END FUNCTIONS TO CALL CLIENTSERVICE METHODS

	public void setNewsReady(boolean newsReady) {
		this.newsReady = newsReady;
		
		System.out.println("NEWS READY");
	}

	public void setPricesReady(boolean pricesReady) {
		if(!this.pricesReady) 
			System.out.println("PRICES READY");
		this.pricesReady = pricesReady;	
	}

	public void setPrices30DayReady(boolean prices30DayReady) {
		this.prices30DayReady = prices30DayReady;
		
		System.out.println("PRICES 30 DAY READY");
	}
	
	public void setStatsReady(boolean statsReady) {
		this.statsReady = statsReady;
		
		System.out.println("STATS READY");
	}
	
	public void setCryptoStatsReady(String crypto, Boolean ready) {
		statsReadyMap.put(crypto, ready);
		
		for(var key : statsReadyMap.keySet())
			if(statsReadyMap.get(key) == false)
				return;
		
		setStatsReady(true);
		ClientView.getInstance().updateAllAssetsOrder();
	}
	
	public void set30DayPricesReady(String crypto, Boolean ready) {
		prices30DaysReadyMap.put(crypto, ready);
		
		for(var key : prices30DaysReadyMap.keySet())
			if(prices30DaysReadyMap.get(key) == false)
				return;
		
		setPrices30DayReady(true);
	}
	
	public void setPricesReady(String crypto, Boolean ready) {
		pricesReadyMap.put(crypto, ready);
		
		for(var key : pricesReadyMap.keySet())
			if(pricesReadyMap.get(key) == false)
				return;
		
		setPricesReady(true);
		updatePnL();
	}
	
	public Boolean statsReady() {
		return statsReady;
	}
	
	private void updatePortfolioStats() {
		if(allReady())
			ClientView.getInstance().updatePortfolioStats();
	}

	public boolean allReady() { //Checks if everything is ready for the app to set Home Screen
		if(!prices30DayReady || !pricesReady  || !newsReady || !statsReady || !balanceReady || !amountOwnedReady || !transactionsReady) 
			return false;
		return true;
	}

	public void updateAmountOwnedPortfolio() {
		ClientView.getInstance().updatePortfolioAmounts(amountOwned);
	}

	public void updatePortfolioValue() {
		if(pricesReady && amountOwnedReady) {
			ClientView.getInstance().updateTotalPortfolioValue();
			updatePortfolioStats();
		}
	}

	public void setTransactions(Vector<Transaction> transactions) throws IOException {
		this.transactions = transactions;
		
		Vector<Transaction> recentTransactions = new Vector<Transaction>();
		
		int cont = 0;
		for(int i=transactions.size()-1; i>=0; i--) {
			if(cont < 10) {
				recentTransactions.add(transactions.elementAt(i));
			}
			else
				break;
			cont++;
		}
		
		transactionsReady = true;
		
		System.out.println("TRANSACTIONS READY");
		
		ClientView.getInstance().setRecentTransactions(recentTransactions);
		updateAssetTransactions();
	}
	
	public void setTemporaryTransaction(Double amount, Double total, String crypto, String buy, String date, String recipient) {
		Transaction temporaryTransact = new Transaction(amount, total, crypto, buy, username, date, recipient);
		this.temporaryTransaction = temporaryTransact;
	}
	
	public void setReceivedTransaction(Double amount, Double total, String crypto, String buy, String date, String recipient, String sender) {
		Transaction temporaryTransact = new Transaction(amount, total, crypto, buy, sender, date, recipient);
		this.temporaryTransaction = temporaryTransact;
	}
	
	public void addTransaction() throws IOException {
		transactions.add(temporaryTransaction);
		
		Vector<Transaction> recentTransactions = new Vector<Transaction>();
		
		int cont = 0;
		for(int i=transactions.size()-1; i>=0; i--) {
			if(cont < 10) {
				recentTransactions.add(transactions.elementAt(i));
			}
			else
				break;
			cont++;
		}
		
		ClientView.getInstance().setRecentTransactions(recentTransactions);
		updateAssetTransactions();
	}

	private void updateAssetTransactions() {
		for(var key : assetTransactions.keySet())
			assetTransactions.get(key).clear();
		
		for(int i=0; i<transactions.size(); i++) 
			if(transactions.elementAt(i).getCrypto()!=null)
				assetTransactions.get(transactions.elementAt(i).getCrypto()).add(transactions.elementAt(i));		
	}

	public void updatePnL() {
		if(transactionsReady)
			ClientView.getInstance().updatePnL(assetTransactions);
	}

	public Double getPortfolioValueYesterday() {
		Double value = 0.0;
		for(var key : amountOwned.keySet()) {
			value += amountOwned.get(key) * Prices.getInstance().getCryptoPrices30Days(key).elementAt(0).getPrice();
		}
		return value;
	}
	
	public Vector<Transaction> getTransactions(){
		return transactions;
	}
}
