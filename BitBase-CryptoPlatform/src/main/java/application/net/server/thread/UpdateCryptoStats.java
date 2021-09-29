package application.net.server.thread;


import java.util.HashMap;
import java.util.Map;
import application.net.common.API;
import application.net.server.data.ThreadCryptoStats;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;

public class UpdateCryptoStats implements Runnable{ //Server requests assets' stats because the API provider usually experiences high volume and has a limited number of requests per minute  so having each client request stats becomes a slow loading operation.
	
	private static Map<String, ThreadCryptoStats> dataServiceCryptoStats;
	
	public UpdateCryptoStats() {
		dataServiceCryptoStats = new HashMap<String, ThreadCryptoStats>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				dataServiceCryptoStats.put(key, new ThreadCryptoStats());
			}
		}
	}

	private void get30DayPrices(ThreadCryptoStats dataServiceCryptoStats, String crypto) {
		dataServiceCryptoStats.setDati(API.getInstance().getStatsAPI(crypto), crypto);
		dataServiceCryptoStats.run();
	}

	@Override
	public void run() {
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				get30DayPrices(dataServiceCryptoStats.get(key), key);
			}
		}
	}
	
}
