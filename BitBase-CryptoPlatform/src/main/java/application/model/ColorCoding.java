package application.model;

import java.util.HashMap;
import java.util.Map;

import application.net.common.Protocol;

public class ColorCoding { //Handles the colors for each asset
	private static ColorCoding instance;
	private Map<String, String> assetMainColor;
	private Map<String, String> assetSecondaryColor;
	private Map<String, String> chartColors;
	private Map<String, String> assetTickerColors;
	
	
	public ColorCoding() {
		assetMainColor = new HashMap<String, String>();
		assetSecondaryColor = new HashMap<String, String>();
		chartColors = new HashMap<String, String>();
		assetTickerColors = new HashMap<String, String>();
		
		setAssetMainColors();
		setAssetSecondaryColors();
		setChartColors();
		setAssetTickerColors();
	}

	public static ColorCoding getInstance(){
		if(instance == null)
			instance = new ColorCoding();
		
		return instance;
	}
	
	private void setAssetTickerColors() {
		assetTickerColors.put(Protocol.BTC, "#ffac54;");
		assetTickerColors.put(Protocol.ETH, "#92aaff;");
		assetTickerColors.put(Protocol.BNB, "#fff678;");
		assetTickerColors.put(Protocol.ADA, "#79fcfc;");
		assetTickerColors.put(Protocol.DOT, "#ff66b7;");
		assetTickerColors.put(Protocol.LTC, "#e3e3e3;");
		assetTickerColors.put(Protocol.XRP, "#a8a8a8;");
		assetTickerColors.put(Protocol.SOL, "#e169ff;");
		assetTickerColors.put(Protocol.TRX, "#ff4251;");
	}
	
	private void setChartColors() {
		chartColors.put(Protocol.BTC, "-data-color-1: #EB8C19;");
		chartColors.put(Protocol.ETH, "-data-color-1: #87a0ff;");
		chartColors.put(Protocol.BNB, "-data-color-1: #ffd05e;");
		chartColors.put(Protocol.ADA, "-data-color-1: #32d9d9;");
		chartColors.put(Protocol.DOT, "-data-color-1: #ff1c94;");
		chartColors.put(Protocol.LTC, "-data-color-1: #f2f2f2;");
		chartColors.put(Protocol.XRP, "-data-color-1: #000000;");
		chartColors.put(Protocol.SOL, "-data-color-1: #dc4fff;");
		chartColors.put(Protocol.TRX, "-data-color-1: #fc0015;");
	}
	
	private void setAssetMainColors() {
		assetMainColor.put(Protocol.USD, "#ffffff;");
		assetMainColor.put(Protocol.BTC, "#ffb254;");
		assetMainColor.put(Protocol.ETH, "#a9bafc;");
		assetMainColor.put(Protocol.BNB, "#ffd05e;");
		assetMainColor.put(Protocol.ADA, "#32d9d9;");
		assetMainColor.put(Protocol.DOT, "#ff1c94;");
		assetMainColor.put(Protocol.LTC, "#f2f2f2;");
		assetMainColor.put(Protocol.XRP, "#d4d4d4;");
		assetMainColor.put(Protocol.SOL, "#e682ff;");
		assetMainColor.put(Protocol.TRX, "#ff5967;");
	}
	
	private void setAssetSecondaryColors() {
		assetSecondaryColor.put(Protocol.BTC, "#e67e00;");
		assetSecondaryColor.put(Protocol.ETH, "#4f74ff;");
		assetSecondaryColor.put(Protocol.BNB, "#f2b92e;");
		assetSecondaryColor.put(Protocol.ADA, "#0dbfbf;");
		assetSecondaryColor.put(Protocol.DOT, "#d1006f;");
		assetSecondaryColor.put(Protocol.LTC, "#c9c9c9;");
		assetSecondaryColor.put(Protocol.XRP, "#bfbfbf;");
		assetSecondaryColor.put(Protocol.SOL, "#d340f7;");
		assetSecondaryColor.put(Protocol.TRX, "#e61e2e;");
	}

	
	public String getPrimaryColor(String asset) {
		return assetMainColor.get(asset);
	}
	
	public String getSecondaryColor(String asset) {
		return assetSecondaryColor.get(asset);
	}

	public String getChartColor(String crypto) {
		return chartColors.get(crypto);
	}
	
	public String getTickerColor(String crypto) {
		return assetTickerColors.get(crypto);
	}
}
