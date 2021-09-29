package application.model;

import java.util.HashMap;
import java.util.Map;

import application.net.common.Protocol;

public class AboutCrypto { //Class containing links and descriptions for each asset
	
	private static AboutCrypto instance = null;;
	Map <String, String> officialWebsites;
	Map <String, String> whitepapers;
	Map <String, String> descriptions;
	
	public AboutCrypto() {
		officialWebsites = new HashMap<String, String>();
		whitepapers = new HashMap<String, String>();
		descriptions = new HashMap<String, String>();
		
		initWebsites();
		initWhitepapers();
		initDescriptions();
	}
	
	public static AboutCrypto getInstance(){
		if(instance == null)
			instance = new AboutCrypto();
		
		return instance ;
	}
	
	private void initWebsites() {
		officialWebsites.put(Protocol.BTC, "https://bitcoin.org/en/");
		officialWebsites.put(Protocol.ETH, "https://ethereum.org/en/");
		officialWebsites.put(Protocol.BNB, "https://www.binance.com/en");
		officialWebsites.put(Protocol.ADA, "https://cardano.org");
		officialWebsites.put(Protocol.DOT, "https://polkadot.network");
		officialWebsites.put(Protocol.LTC, "https://litecoin.org");
		officialWebsites.put(Protocol.XRP, "https://xrpl.org");
		officialWebsites.put(Protocol.SOL, "https://solana.com");
		officialWebsites.put(Protocol.TRX, "https://tron.network");
	}

	private void initWhitepapers() {
		whitepapers.put(Protocol.BTC, "https://bitcoin.org/bitcoin.pdf");
		whitepapers.put(Protocol.ETH, "https://ethereum.org/en/whitepaper/");
		whitepapers.put(Protocol.BNB, "https://whitepaper.io/document/10/binance-whitepaper");
		whitepapers.put(Protocol.ADA, "https://docs.cardano.org/introduction");
		whitepapers.put(Protocol.DOT, "https://polkadot.network/PolkaDotPaper.pdf");
		whitepapers.put(Protocol.LTC, "https://whitepaper.io/document/683/litecoin-whitepaper");
		whitepapers.put(Protocol.XRP, "https://ripple.com/files/ripple_consensus_whitepaper.pdf"); 
		whitepapers.put(Protocol.SOL, "https://solana.com/solana-whitepaper.pdf");
		whitepapers.put(Protocol.TRX, "https://developers.tron.network/docs");
	}
	
	private void initDescriptions() {
		descriptions.put(Protocol.BTC, "The world's first cryptocurrency, Bitcoin is stored and exchanged securely on the internet through a digital ledger known as a blockchain. Bitcoins are divisible into smaller units known as satoshis - each satoshi is worth 0.00000001 bitcoin.");
		descriptions.put(Protocol.ETH, "Ethereum is a decentralized computing platform that uses ETH (also called Ether) to pay transaction fees (or 'gas'). Developers can use Ethereum to run decentralized applications (dApps) and issue new crypto assets, known as Ethereum tokens.");
		descriptions.put(Protocol.BNB, "Binance is one of the biggest cryptocurrency exchanges globally. By aiming to bring cryptocurrency exchanges to the forefront of financial activity globally. The idea behind Binance's name is to show this new paradigm in global finance - Binary Finance, or Binance.");
		descriptions.put(Protocol.ADA, "Cardano (ADA) is a blockchain platform built on a proof-of-stake consensus protocol that validates transactions without high energy costs. Development on Cardano uses the Haskell programming language. ADA is named after the 19th century mathematician, Ada Lovelace.");
		descriptions.put(Protocol.DOT, "Polkadot is a protocol that enables cross-blockchain transfers of any type of data or asset. By uniting multiple blockchains, Polkadot aims to achieve high degrees of security and scalability.");
		descriptions.put(Protocol.LTC, "Litecoin is a cryptocurrency that uses a faster payment confirmation schedule and a different cryptographic algorithm than Bitcoin.");
		descriptions.put(Protocol.XRP, "XRP is the currency that runs on a digital payment platform called RippleNet, which is on top of a distributed ledger database called XRP Ledger. RippleNet is run by Ripple, the XRP Ledger is open-source and is based on ledger database.");
		descriptions.put(Protocol.SOL, "Solana is a highly functional open source project that banks on blockchain technology's permissionless nature to provide decentralized finance (DeFi) solutions. Solana was officially launched in March 2020 by the Solana Foundation.");
		descriptions.put(Protocol.TRX, "TRON is a blockchain-based operating system that aims to ensure this technology is suitable for daily use. Whereas Bitcoin can handle up to six transactions per second, and Ethereum up to 25, TRON claims that its network has capacity for 2,000 TPS.");
		
	}


	public String getWebsite(String crypto) {
		return officialWebsites.get(crypto);
	}
	
	public String getWhitepaper(String crypto) {
		return whitepapers.get(crypto);
	}
	
	public String getDescription(String crypto) {
		return descriptions.get(crypto);
	}
}
