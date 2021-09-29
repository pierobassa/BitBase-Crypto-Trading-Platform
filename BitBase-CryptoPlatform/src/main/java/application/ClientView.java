package application;


import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import application.controller.AllAssetsController;
import application.controller.AssetController;
import application.controller.CryptoScreenController;
import application.controller.HomeController;
import application.controller.LoadingScreenContoller;
import application.controller.PortfolioController;
import application.controller.SingleAssetController;
import application.model.ClientLogic;
import application.model.News;
import application.model.PriceDate;
import application.net.common.Protocol;
import application.net.common.Transaction;
import javafx.util.Pair;


public class ClientView { //Handles the various controllers of the user interface
	
	private HomeController homeController;
	private CryptoScreenController cryptoScreenController;
	private LoadingScreenContoller loadingScreenController;
	private PortfolioController portfolioController;
	private AllAssetsController allAssetsController;
	
	private Map<String, SingleAssetController> portfolioAssetsControllers;
	private Map<String, AssetController> allAssetsControllers;
	private static ClientView instance = null;
	
	private Vector<Pair<String, Double>> holdingValues;
	private Double totalSpent;
	
	
	ClientView(){
		portfolioAssetsControllers = new HashMap<String, SingleAssetController>();
		allAssetsControllers = new HashMap<String, AssetController>();
	}
	
	public static ClientView getInstance(){
		if(instance == null)
			instance  = new ClientView();
		
		return instance;
	}
	
	public void addAssetController(String asset, SingleAssetController controller) {
		portfolioAssetsControllers.put(asset, controller);
	}
	
	public void addSingleAssetController(String asset, AssetController controller) {
		allAssetsControllers.put(asset, controller);
	}
	
	public SingleAssetController getAssetController(String asset) {
		return  portfolioAssetsControllers.get(asset);
	}
	
	public void setHomeController(HomeController homeController) {
		this.homeController = homeController;
	}
	
	public void setPortfolioController(PortfolioController portfolioController) {
		this.portfolioController = portfolioController;
	}
	
	
	public void setCryptoScreenControlelr(CryptoScreenController cryptoScreenController) {
		this.cryptoScreenController = cryptoScreenController;
	}
	
	public void setLoadingController(LoadingScreenContoller loadingScreenController) {
		this.loadingScreenController = loadingScreenController;
	}
	
	public void initCrypto(String crypto, Vector<PriceDate> prices) {
		switch(crypto) {
			case Protocol.LTC:{
				homeController.initLTC(prices);
				break;
			}
			case Protocol.DOT:{
				homeController.initDOT(prices);
				break;
			}
			case Protocol.ADA:{
				homeController.initADA(prices);
				break;
			}
			case Protocol.BNB:{
				homeController.initBNB(prices);
				break;
			}
			case Protocol.ETH:{
				homeController.initETH(prices);
				break;
			}
			case Protocol.BTC:{
				homeController.initBTC(prices);
				break;
			}
			default:{
				break;
			}
		}
	}
	
	public void setPrice(String crypto, Double price) {
		switch(crypto) {
			case Protocol.LTC:{
				homeController.setLTCprice(price);		
				break;
			}
			case Protocol.DOT:{
				homeController.setDOTprice(price);		
				break;
			}
			case Protocol.ADA:{
				homeController.setADAprice(price);	
				break;
			}
			case Protocol.BNB:{
				homeController.setBNBprice(price);	
				break;
			}
			case Protocol.ETH:{
				homeController.setETHprice(price);	
				break;
			}
			case Protocol.BTC:{
				homeController.setBTCprice(price);	
				break;
			}
			default:{
				break;
			}
		}
		
		portfolioAssetsControllers.get(crypto).setPrice(price);
		allAssetsControllers.get(crypto).setPrice(price);
	}
	
	public void setNewsHome(Vector<News> news) {
		homeController.setNewsHome(news);
	}
	
	public void updateBalance() {
		if(cryptoScreenController!=null)
			cryptoScreenController.updateBalance();
	}
	
	public void reset() {		
		loadingScreenController.stop();
		homeController.stopNewsChanger();
	}
	
	public void updatePortfolioAmounts(Map<String, Double> amountOwned) {
		for(var key : amountOwned.keySet()) {
			portfolioAssetsControllers.get(key).setValues(amountOwned.get(key));
		}
	}
	
	public void updateTotalPortfolioValue() {
		Vector<Pair<String, Double>> holdingValues = new Vector<Pair<String, Double>>();
		Double value = 0.0;
		for(var key : portfolioAssetsControllers.keySet()) {
			if(key!=Protocol.USD)
				portfolioAssetsControllers.get(key).updateholdingValue(); //When a transaction is done, we need to update the holding values otherwise the total portfolio value would be incorrect until prices are updated.
			value += portfolioAssetsControllers.get(key).getHoldingValue();
			holdingValues.add(new Pair<String, Double>(key, portfolioAssetsControllers.get(key).getHoldingValue()));
		}
		
		portfolioController.setPortfolioValue(value); //Portfolio value is retrieved adding all the current values of the assets owned.
		
		holdingValues.sort(new Comparator<Pair<String, Double>>() {

			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				// TODO Auto-generated method stub
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		this.holdingValues = holdingValues; //holdinValues used for the pieChart
	}
	
	public void assetPercentageChanges() {
		for(var key : portfolioAssetsControllers.keySet()) 
			if(key != Protocol.USD)
				portfolioAssetsControllers.get(key).percentageChange();
	}
	
	public void updatePercentageChanges() {
		for(var key : allAssetsControllers.keySet()) 
			allAssetsControllers.get(key).percentageChange();
	}
	
	public void updatePieChart() {
		portfolioController.updatePieChart(holdingValues);
	}
	
	public void setAssetStats() {
		for(var key : allAssetsControllers.keySet()) 
			allAssetsControllers.get(key).setStats();
	}

	public void setRecentTransactions(Vector<Transaction> transactions) throws IOException {
		portfolioController.setRecentTransactions(transactions);
	}

	public void updatePnL(Map<String, Vector<Transaction>> assetTransactions) {
		Double spentValue = 0.0;
		Double totalSpent = 0.0;
		
		
		for(var key : assetTransactions.keySet()) {
			for(int i=0; i<assetTransactions.get(key).size(); i++) {
				if(assetTransactions.get(key).elementAt(i).getUsername().equals(ClientLogic.getInstance().getUsername())) { //IF TRANSACTION DONE BY USER
					if(assetTransactions.get(key).elementAt(i).getBuy().equals(Protocol.BUY)) { //Spent value increases when you buy
						spentValue += assetTransactions.get(key).elementAt(i).getTotal();
						totalSpent += assetTransactions.get(key).elementAt(i).getTotal();
					}
					if(assetTransactions.get(key).elementAt(i).getBuy().equals(Protocol.SELL)) { //Spent value decreases when you sell. E.g: you buy 1 BTC at 1000 dollars. And sell 1 BTC at 9000 dollars. You spent 1000. If you sold at 1100 you gained 1000.
						spentValue -= assetTransactions.get(key).elementAt(i).getTotal();
						totalSpent -= assetTransactions.get(key).elementAt(i).getTotal();
					}
				}
			}

			portfolioAssetsControllers.get(key).updatePnL(spentValue); //Updated Profit / Loss of the asset in portfolio
			
			spentValue = 0.0;
		}
		
		
		this.totalSpent = totalSpent; //Total spent needed to understand the All time profit / loss.
	}

	public void updatePortfolioStats() { //Looks for the best performing asset and worst performing asset in terms of profit / loss.
		if(totalSpent != null)
			portfolioController.updateAllTimePnL(totalSpent);
		
		String bestPerformer = null;
		Double bestPerformerValue = Double.MAX_VALUE * -1;
		String worstPerformer = null;
		Double worstPerformerValue = Double.MAX_VALUE;
		
		for(var key : portfolioAssetsControllers.keySet()){
			Double pnl = portfolioAssetsControllers.get(key).getPnL();
		
			if(key.equals(Protocol.USD))
				continue;
			if(pnl>bestPerformerValue && ClientLogic.getInstance().getAmountOwned(key) > 0.00001) {
				bestPerformer = key;
				bestPerformerValue = pnl;
			}
			if(pnl<worstPerformerValue && ClientLogic.getInstance().getAmountOwned(key) > 0.00001) {
				worstPerformer = key;
				worstPerformerValue = pnl;
			}
		}
		
		if(bestPerformer != null && worstPerformer != null && ClientLogic.getInstance().allReady())
			portfolioController.updateBestAndWorstPerferomers(bestPerformer, worstPerformer);
		else
			portfolioController.resetBestAndWorstPerformers();			
	}
	
	public void setAllAssetsController(AllAssetsController controller) {
		this.allAssetsController = controller;
	}

	
	public Double assetChange(String asset) {
		return portfolioAssetsControllers.get(asset).getPnLChange();
	}

	public Double assetPnL(String asset) {
		return portfolioAssetsControllers.get(asset).getPnL();
	}

	public String getAmount(String key) {
		return portfolioAssetsControllers.get(key).getAmount();
	}
	
	public Double getHoldingValue(String key) {
		return portfolioAssetsControllers.get(key).getHoldingLblValue();
	}
	
	public void updateOrder() throws IOException {
		portfolioController.updateOrder();
	}

	public void updateAllAssetsOrder() {
		allAssetsController.updateOrder();
	}

	public void changeUsername(String newUsername) {
		homeController.setUsername(newUsername);
	}

	public void playNewsTransition() {
		homeController.playNewsTransition();
	}
	

	
}
