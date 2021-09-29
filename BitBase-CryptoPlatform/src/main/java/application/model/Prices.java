package application.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import application.net.common.Stats;

public class Prices { //Holds prices and stats of each asset
	
	private static Map <String, Vector<PriceDate>> cryptoPrices30Days; 
	private static Map <String, Double> cryptoCurrentPrice; 
	private static Map <String, Stats> cryptoStats;
	
	private static Prices instance = null;
	
	public Prices() {
		cryptoPrices30Days = new HashMap<String, Vector<PriceDate>>();
		cryptoCurrentPrice = new HashMap<String, Double>();
		cryptoStats = new HashMap<String, Stats>();
	}
	
	public static Prices getInstance(){
		if(instance == null)
			instance = new Prices();
		
		return instance;
	}
	
	public void addPrices30Days(String crypto, Vector<PriceDate> prices) { //crypto using Protcol.Crypto
		cryptoPrices30Days.put(crypto, prices);
	}
	
	public void setCurrentPrice(String crypto, Double price) {
		cryptoCurrentPrice.put(crypto, price);
	}
	
	public void setStats(String crypto, Stats stats) {
		cryptoStats.put(crypto, stats);
	}
	
	public Vector<PriceDate> getCryptoPrices30Days(String crypto) {
		return cryptoPrices30Days.get(crypto);	
	}
	
	public Double getCryptoCurrentPrice(String crypto) {
		return cryptoCurrentPrice.get(crypto);
	}
	
	public Stats getCryptoStats(String crypto) {
		return cryptoStats.get(crypto);
	}

	public void reset() {
		cryptoPrices30Days.clear();
		cryptoCurrentPrice.clear();
		cryptoStats.clear();
	}
}
