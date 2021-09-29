package application.net.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class SupportedAssets implements Serializable { //All the supported assets. Adding/editing/removing an asset becomes very simple. 

	private static final long serialVersionUID = -7618849218072317226L;
	private static SupportedAssets instance = null;
	Map<String, String> names;
	Map<String, Image> iconsSmall;
	Map<String, Image> iconsLarge;
	
	public SupportedAssets() {
		names = new LinkedHashMap<String, String>();
		
		iconsSmall = new HashMap<String, Image>();
		iconsLarge = new HashMap<String, Image>();
		
		setNames();
		setIconsSmall();
		setIconsLarge();
	}


	public static SupportedAssets getInstance(){
		if(instance == null)
			instance = new SupportedAssets();
		
		return instance ;
	}

	
	private void setIconsSmall() {
		iconsSmall.put(Protocol.USD, new Image(getClass().getResourceAsStream("/images/icons/usd-icon-portfolio.png")));
		iconsSmall.put(Protocol.BTC, new Image(getClass().getResourceAsStream("/images/icons/bitcoin-icon-portfolio.png")));
		iconsSmall.put(Protocol.ETH, new Image(getClass().getResourceAsStream("/images/icons/ethereum-icon-portfolio.png")));
		iconsSmall.put(Protocol.BNB, new Image(getClass().getResourceAsStream("/images/icons/binance-icon-portfolio.png")));
		iconsSmall.put(Protocol.ADA, new Image(getClass().getResourceAsStream("/images/icons/cardano-icon-portfolio.png")));
		iconsSmall.put(Protocol.DOT, new Image(getClass().getResourceAsStream("/images/icons/polkadot-icon-portfolio.png")));
		iconsSmall.put(Protocol.LTC, new Image(getClass().getResourceAsStream("/images/icons/litecoin-icon-portfolio.png")));
		iconsSmall.put(Protocol.XRP, new Image(getClass().getResourceAsStream("/images/icons/ripple-icon-portfolio.png")));
		iconsSmall.put(Protocol.SOL, new Image(getClass().getResourceAsStream("/images/icons/solana-icon-portfolio.png")));
		iconsSmall.put(Protocol.TRX, new Image(getClass().getResourceAsStream("/images/icons/tron-icon-portfolio.png")));
	}
	
	public Image getSmallIcon(String asset) {
		return iconsSmall.get(asset);
	}

	private void setIconsLarge() {
		iconsLarge.put(Protocol.BTC, new Image(getClass().getResourceAsStream("/images/icons/bitcoin-icon.png")));
		iconsLarge.put(Protocol.ETH, new Image(getClass().getResourceAsStream("/images/icons/ethereum-icon.png")));
		iconsLarge.put(Protocol.BNB, new Image(getClass().getResourceAsStream("/images/icons/binance-icon.png")));
		iconsLarge.put(Protocol.ADA, new Image(getClass().getResourceAsStream("/images/icons/cardano-icon.png")));
		iconsLarge.put(Protocol.DOT, new Image(getClass().getResourceAsStream("/images/icons/polkadot-icon.png")));
		iconsLarge.put(Protocol.LTC, new Image(getClass().getResourceAsStream("/images/icons/litecoin-icon.png")));
		iconsLarge.put(Protocol.XRP, new Image(getClass().getResourceAsStream("/images/icons/ripple-icon.png")));
		iconsLarge.put(Protocol.SOL, new Image(getClass().getResourceAsStream("/images/icons/solana-icon.png")));
		iconsLarge.put(Protocol.TRX, new Image(getClass().getResourceAsStream("/images/icons/tron-icon.png")));
	}
	
	public Image getLargeIcon(String asset) {
		return iconsLarge.get(asset);
	}

	private void setNames() {
		names.put(Protocol.USD, "Balance");
		names.put(Protocol.BTC, "Bitcoin");
		names.put(Protocol.ETH, "Ethereum");
		names.put(Protocol.BNB, "Binance");
		names.put(Protocol.ADA, "Cardano");
		names.put(Protocol.DOT, "Polkadot");
		names.put(Protocol.LTC, "Litecoin");
		names.put(Protocol.XRP, "Ripple");
		names.put(Protocol.SOL, "Solana");
		names.put(Protocol.TRX, "Tron");
	}
	
	public Map<String, String> getAssets(){
		return names;
	}
	
	public String getName(String asset) {
		return names.get(asset);
	}
	
}
