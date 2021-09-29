package application.net.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class API implements Serializable{ //Handles the data API's that result in prices, statistics and news.

	private static final long serialVersionUID = -3676405060817337224L;
	private static API instance = null;
	private String APIkey = "YJW56YORXKPMWFDU8RM3"; //Apikey needed to use cryptowat.ch public API
	private String newsAPIkey = "5d711f7e4dc7434bb70e91331a269d34"; //newsapi.org API KEY
	private String newsAPI = "https://newsapi.org/v2/everything?domains=coingecko.com,cointelegraph.com&apiKey=" + newsAPIkey;
	private Map <String, String> cryptoPrices30DaysAPI; 
	private Map <String, String> cryptoCurrentPriceAPI; 
	private Map <String, String> cryptoStatsAPI;
	
	public API() {
		cryptoPrices30DaysAPI = new HashMap<String, String>();
		cryptoCurrentPriceAPI = new HashMap<String, String>();
		cryptoStatsAPI = new HashMap<String, String>();
		initialize30DayAPIs();
		initializePriceAPIs();
		initializeStatsAPIs();
	}

	public static API getInstance(){
		if(instance == null)
			instance  = new API();
		
		return instance;
	}

	private void initialize30DayAPIs() {
		cryptoPrices30DaysAPI.put(Protocol.BTC, "https://api.cryptowat.ch/markets/binance/btcusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.ETH, "https://api.cryptowat.ch/markets/binance/ethusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.BNB, "https://api.cryptowat.ch/markets/binance/bnbusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.ADA, "https://api.cryptowat.ch/markets/binance/adausdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.DOT, "https://api.cryptowat.ch/markets/binance/dotusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.LTC, "https://api.cryptowat.ch/markets/binance/ltcusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.XRP, "https://api.cryptowat.ch/markets/binance/xrpusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.SOL, "https://api.cryptowat.ch/markets/binance/solusdt/ohlc?apikey=" + APIkey);
		cryptoPrices30DaysAPI.put(Protocol.TRX, "https://api.cryptowat.ch/markets/binance/trxusdt/ohlc?apikey=" + APIkey);
	}

	private void initializePriceAPIs() {
		cryptoCurrentPriceAPI.put(Protocol.BTC, "https://api.cryptowat.ch/markets/binance/btcusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.ETH, "https://api.cryptowat.ch/markets/binance/ethusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.BNB, "https://api.cryptowat.ch/markets/binance/bnbusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.ADA, "https://api.cryptowat.ch/markets/binance/adausdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.DOT, "https://api.cryptowat.ch/markets/binance/dotusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.LTC, "https://api.cryptowat.ch/markets/binance/ltcusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.XRP, "https://api.cryptowat.ch/markets/binance/xrpusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.SOL, "https://api.cryptowat.ch/markets/binance/solusdt/price?apikey=" + APIkey);
		cryptoCurrentPriceAPI.put(Protocol.TRX, "https://api.cryptowat.ch/markets/binance/trxusdt/price?apikey=" + APIkey);
	}
	
	private void initializeStatsAPIs() {
		cryptoStatsAPI.put(Protocol.BTC, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=bitcoin");
		cryptoStatsAPI.put(Protocol.ETH, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=ethereum");
		cryptoStatsAPI.put(Protocol.BNB, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=binancecoin");
		cryptoStatsAPI.put(Protocol.ADA, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=cardano");
		cryptoStatsAPI.put(Protocol.DOT, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=polkadot");
		cryptoStatsAPI.put(Protocol.LTC, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=litecoin");
		cryptoStatsAPI.put(Protocol.XRP, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=ripple");
		cryptoStatsAPI.put(Protocol.SOL, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=solana");
		cryptoStatsAPI.put(Protocol.TRX, "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=tron");
	}

	public String get30DayAPI(String crypto) {
		return cryptoPrices30DaysAPI.get(crypto);
	}
	
	public String getLivePriceAPI(String crypto) {
		return cryptoCurrentPriceAPI.get(crypto);
	}

	public String getNewsAPI() {
		return newsAPI;
	}
	
	public String getStatsAPI(String crypto) {
		return cryptoStatsAPI.get(crypto);
	}
}
