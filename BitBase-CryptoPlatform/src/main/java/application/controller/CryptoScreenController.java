package application.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import application.ClientView;
import application.DepositDialog;
import application.MsgDialog;
import application.MyAccountDialog;
import application.SceneHandler;
import application.model.AboutCrypto;
import application.model.ClientLogic;
import application.model.ColorCoding;
import application.model.PriceDate;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CryptoScreenController implements Initializable{
	
	@FXML
    private Pane menuPane;

    @FXML
    private Pane homeBtn;

    @FXML
    private ImageView homeIcon;

    @FXML
    private Pane portfolioBtn;

    @FXML
    private ImageView portfolioIcon;

    @FXML
    private Pane tradeBtn;

    @FXML
    private ImageView tradeIcon;

    @FXML
    private ImageView homelogoImg;

    @FXML
    private Pane logoutBtn;

    @FXML
    private ImageView logoutIcon;

    @FXML
    private Pane welcomePane;

    @FXML
    private Pane myAccountPane;

    @FXML
    private Label myAccountBtn;

    @FXML
    private ImageView accountIcon;

    @FXML
    private ImageView cryptoIcon;
    
    @FXML
    private Pane balancePane;

    @FXML
    private Pane depositBtn;

    @FXML
    private Pane chartPane;

    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Pane marketCapPane;

    @FXML
    private Label marketCapLbl;

    @FXML
    private Label marketCapValueLbl;

    @FXML
    private Pane volumePane;

    @FXML
    private Label volumeLbl;

    @FXML
    private Label volumeValueLbl;

    @FXML
    private Pane supplyPane;

    @FXML
    private Label supplyLbl;

    @FXML
    private Label supplyValueLbl;

    @FXML
    private Pane transactPane;

    @FXML
    private Pane buyBtn;

    @FXML
    private Pane sellBtn;

    @FXML
    private Label transactLbl;

    @FXML
    private TextField amountField;

    @FXML
    private Label amountLbl;

    @FXML
    private Button buySellBtn;

    @FXML
    private TextField totalField;

    @FXML
    private Label totalLbl;

    @FXML
    private Label errorLbl;

    @FXML
    private Label balanceLbl;

    @FXML
    private ImageView balanceIcon;
    
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private Pane aboutPane;

    @FXML
    private Label aboutLbl;

    @FXML
    private Pane officialWebsiteBtn;

    @FXML
    private ImageView websiteIcon;

    @FXML
    private Pane whitepaperBtn;

    @FXML
    private ImageView whitepaperIcon;
    

    @FXML
    private Label priceLbl;
    
    @FXML
    private Label cryptoLbl;

    @FXML
    private Label cryptoTicker;


    @FXML
    private Label percentageDayLbl;
    
    @FXML
    private ImageView aboutImg;
    
    @FXML
    private Button btn25;

    @FXML
    private Button btn50;

    @FXML
    private Button btn75;

    @FXML
    private Button btn100;
    
    @FXML
    private ImageView mCapImg;
    
    @FXML
    private Label aboutName;
    
    @FXML
    private ImageView volumeImg;
    
    @FXML
    private ImageView supplyImg;
	    
	private String crypto;
	private Vector<PriceDate> prices;
	    
	private Task<Void> livePriceTask;
	   
	private Timeline livePrice;
	    
	private Double balance;
	private Double price;
	    
	private boolean buy;
	    
	public CryptoScreenController(String crypto) {
		this.crypto = crypto;
	    this.price = Prices.getInstance().getCryptoCurrentPrice(crypto);
	    this.prices = Prices.getInstance().getCryptoPrices30Days(crypto);
	}
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    buy = true;
		errorLbl.setText("");
			
		setImages();
		setBinds();
		getBalance();
		setInitStyling();
		setConstraints();
		setInitValues();
	}


	private void setInitValues() {
		balance = ClientLogic.getInstance().getBalance();
		balance = round(balance, 3);
		balanceLbl.setText("$"+balance);
		
		Double marketCap = Prices.getInstance().getCryptoStats(crypto).getMarket_cap();
		marketCap = marketCap / Math.pow(10, 9);
		BigDecimal d = new BigDecimal(marketCap);
		d = d.setScale(2, RoundingMode.DOWN); 
		marketCapValueLbl.setText("$"+d.toPlainString()+ " B");
		
		Double circulatingSupply = Prices.getInstance().getCryptoStats(crypto).getCirculating_supply();
		circulatingSupply = circulatingSupply / Math.pow(10, 6);
		d = new BigDecimal(circulatingSupply);
		d = d.setScale(3, RoundingMode.DOWN); 
		
		
		supplyValueLbl.setText(d.toPlainString() + "M " + crypto);
		marketCapLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getTickerColor(crypto));
		volumeLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getTickerColor(crypto));
		supplyLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getTickerColor(crypto));
		aboutLbl.setText(AboutCrypto.getInstance().getDescription(crypto));
		aboutName.setText("About " + SupportedAssets.getInstance().getName(crypto));
		
		
		Double volume = Prices.getInstance().getCryptoStats(crypto).getVolume_24h();
		volume = volume / Math.pow(10, 9);
		d = new BigDecimal(volume);
		d = d.setScale(2, RoundingMode.DOWN); //Otherwise there are too many decimal units
		volumeValueLbl.setText("$"+d.toPlainString() + " B");
	}

	private void setConstraints() { //Constraints regarding the amountField 
		
		amountField.lengthProperty().addListener(new ChangeListener<Number>() {
			
			final int LIMIT = 10;

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
                    if (amountField.getText().length() >= LIMIT) {

                        // if it's 11th character then just setText to previous
                        amountField.setText(amountField.getText().substring(0, LIMIT));
                    }
                    
                    String txt = amountField.getText();
                    
                    if(!Character.isDigit(txt.charAt(txt.length()-1)) && txt.charAt(txt.length()-1) != '.') //Characters that are not digits are not valid, besides '.'
                    	amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(txt.length()==1 && txt.charAt(0)=='.') //Can't start with '.'
                    	amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(txt.chars().filter(ch -> ch == '.').count() == 2) //Check if user inserted another '.' which is not a valid number format. Ex: '2.2412.' is invalid
                    	 amountField.setText(amountField.getText().substring(0, amountField.getText().length()-1));
                    
                    if(amountField.getText().length()!=0) //Real time total USD value
                    	updateTotal();
                    
                }
				
				if (newValue.intValue() < oldValue.intValue()) { 
                    if(amountField.getText().length()!=0)
                    	updateTotal();
                    
                    if(amountField.getText().length()==0)
                    	totalField.setText("");
                }
			}
		});
	}

	private void setInitStyling() {
		chart.setStyle(String.format(ColorCoding.getInstance().getChartColor(crypto))); 
		
		priceLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getPrimaryColor(crypto));
		priceLbl.setText("");
		
		cryptoLbl.setText(SupportedAssets.getInstance().getName(crypto).toUpperCase());
		
		cryptoTicker.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getTickerColor(crypto));
		cryptoTicker.setText(crypto);
		
		transactLbl.setText("Buy " + SupportedAssets.getInstance().getName(crypto));
		
		loadChart(crypto);
		updateprice(crypto);		
	}
	

	private void getBalance() {
			String balanceStr = balanceLbl.getText();
			balanceStr = balanceStr.replace("$", "");
			balance = Double.valueOf(balanceStr);
	}
	
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	private void updateTotal() {
			if(!amountField.getText().isEmpty()) {
				String amountStr = amountField.getText();
				amountStr = amountStr.replace(",", ".");
				Double amount = Double.valueOf(amountStr);
				Double total = price * amount;
				
				total = round(total, 3);
				
				BigDecimal d = new BigDecimal(total);
				
				d = d.setScale(3, RoundingMode.DOWN);
				
				totalField.setText(d.toPlainString());
			}
		}        
	
	private void updateprice(String crypto) { //Get's the price of the specific asset every 5 seconds
		 livePriceTask = new Task<Void>() {
	    	 private void stop() {
	    			livePrice.stop();
	    			this.cancel();
	    		}

			@Override
			protected Void call() throws Exception {
				livePrice = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
					
		    	 	@Override
		    	 	public void handle(ActionEvent event) {
		    	 		price = Prices.getInstance().getCryptoCurrentPrice(crypto);
		    	 		if(price == null) {
    						stop();   
    						return;
    					}
		    	 		
		    	 		String priceNow = priceLbl.getText();
    					priceNow = priceNow.replace("$", "");
    					
    					Double dblPriceNow = Double.valueOf(priceNow);                					
    					
    					if(price > dblPriceNow) { //Lighter color if price is higher than previous, darker color otherwise.
    						priceLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getPrimaryColor(crypto));
    					}
    					else {
    						priceLbl.setStyle("-fx-text-fill: " + ColorCoding.getInstance().getSecondaryColor(crypto));
    					}	
    					
    					priceLbl.setText("$"+ String.valueOf(price));
    					
    					updateTotal();
    				
    					
    					percentageChange(prices, price);
		    	 	}
		    	 	
		     }));
				livePrice.setCycleCount(Timeline.INDEFINITE);
				livePrice.play(); 
				return null;
			}
	     };
	     livePriceTask.run(); //Runs until the user leaves the crypto screen page
	}
	
	private void loadChart(String crypto) {
		chart.getData().clear();
		
		Vector<PriceDate> pricesCopy = new Vector<>(prices);
		
		price = Prices.getInstance().getCryptoCurrentPrice(crypto);
		priceLbl.setText("$"+price);
		
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); //Series for the data
		for(int i=prices.size()-1; i>=0; i--) {
			series.getData().add(new XYChart.Data<String, Number>(prices.elementAt(i).getDate(), prices.elementAt(i).getPrice()));
		}
		
		pricesCopy.sort(new ComparatorePrices());
		yAxis.setTickUnit(pricesCopy.elementAt(0).getPrice()*0.01);
		yAxis.setLowerBound(pricesCopy.elementAt(0).getPrice() - pricesCopy.elementAt(0).getPrice()*0.01);
		yAxis.setUpperBound(pricesCopy.elementAt(pricesCopy.size()-1).getPrice() + pricesCopy.elementAt(0).getPrice()*0.01);

		chart.getData().add(series);
	    chart.setCreateSymbols(false); 
	    
	    percentageChange(prices, price);
	}



	private void percentageChange(Vector<PriceDate> prices, Double priceNow) { //Percentage change of the asset compared to yesterday's price
		Double yesterdayPrice = prices.elementAt(0).getPrice();
		if(priceNow>=yesterdayPrice) {
			percentageDayLbl.setStyle("-fx-text-fill:  #3df50a;");
			Double percentageChange = 100 - (yesterdayPrice*100 / priceNow);
			percentageChange = round(percentageChange, 2);
			percentageDayLbl.setText("+"+percentageChange+"%");
		}
		else {
			percentageDayLbl.setStyle("-fx-text-fill:  #ff5353;");
			Double percentageChange = (yesterdayPrice*100 / priceNow) - 100;
			percentageChange = round(percentageChange, 2);
			percentageDayLbl.setText("-"+percentageChange+"%");
		}
	}

	private void setImages() {
		Image image;
		
		cryptoIcon.setImage(SupportedAssets.getInstance().getLargeIcon(crypto));
		image = new Image(getClass().getResourceAsStream("/images/homepagelogo.png"));
	    homelogoImg.setImage(image);	
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/home-icon.png"));
	    homeIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/about.png"));
	    aboutImg.setImage(image);	
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/chart-icon.png"));
	    portfolioIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/trade-icon.png"));
	    tradeIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/user-icon.png"));
	    accountIcon.setImage(image);
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/logout-icon.png"));
	    logoutIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/balance-icon.png"));
	    balanceIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/website-icon.png"));
	    websiteIcon.setImage(image);
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/whitepaper-icon.png"));
	    whitepaperIcon.setImage(image); 
	     
	    image = new Image(getClass().getResourceAsStream("/images/icons/small-info-icon.png"));
	    supplyImg.setImage(image);
	    volumeImg.setImage(image);
	    mCapImg.setImage(image);
	}

	private void setBinds() {
		 myAccountPane.layoutXProperty().bind(welcomePane.widthProperty().subtract(180));
	     depositBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(370));
	     logoutBtn.layoutYProperty().bind(menuPane.heightProperty().subtract(70));	
	     
	     chartPane.prefWidthProperty().bind(transactPane.layoutXProperty().subtract(255));
	     chartPane.prefHeightProperty().bind(aboutPane.layoutYProperty().subtract(270));
	     
	     marketCapPane.layoutYProperty().bind(chartPane.heightProperty().subtract(78));
	     supplyPane.layoutXProperty().bind(chartPane.widthProperty().divide(2).subtract(supplyPane.widthProperty().divide(2)).subtract(25));
	     supplyPane.layoutYProperty().bind(chartPane.heightProperty().subtract(78));
	     volumePane.layoutXProperty().bind(chartPane.widthProperty().subtract(220));
	     volumePane.layoutYProperty().bind(chartPane.heightProperty().subtract(78));
	     
	     chart.prefWidthProperty().bind(chartPane.widthProperty().subtract(10));
	     chart.prefHeightProperty().bind(chartPane.heightProperty().subtract(90));
	     
	     aboutLbl.prefWidthProperty().bind(aboutPane.widthProperty().subtract(242));;
	     
	     transactLbl.layoutXProperty().bind(transactPane.widthProperty().divide(2).subtract(transactLbl.widthProperty().divide(2)));
	     
	     percentageDayLbl.layoutXProperty().bind(priceLbl.layoutXProperty().add(priceLbl.widthProperty()).add(8));
	     priceLbl.layoutXProperty().bind(cryptoTicker.layoutXProperty().add(cryptoTicker.widthProperty()).add(15));
	     cryptoTicker.layoutXProperty().bind(cryptoLbl.layoutXProperty().add(cryptoLbl.widthProperty()).add(8));
	     
	     balancePane.prefWidthProperty().bind(balanceIcon.fitWidthProperty().add(balanceLbl.widthProperty()).add(46));
	}
	
	
	@FXML
	public void switchToSell() {
		buy = false;
		sellBtn.setStyle("-fx-background-color:  transparent; ");
		buyBtn.setStyle("-fx-background-color:   #0F1D26; ");
		
		transactLbl.setText("Sell " +  SupportedAssets.getInstance().getName(crypto));

		
		buySellBtn.setStyle("-fx-background-color:  #ff5353;");
		buySellBtn.setText("Sell");	
		
		amountField.setText("");
		totalField.setText("");
		
		balance = ClientLogic.getInstance().getAmountOwned(crypto);
		balance = round(balance, 5);
		
		BigDecimal d = new BigDecimal(balance);
		
		d = d.setScale(5, RoundingMode.DOWN);
		
		balanceLbl.setText(d.toPlainString() + " " + crypto);
	}
	
	@FXML
	public void switchToBuy() {
		buy = true;
		buyBtn.setStyle("-fx-background-color:  transparent; ");
		sellBtn.setStyle("-fx-background-color:   #0F1D26; ");
		
		transactLbl.setText("Buy " +  SupportedAssets.getInstance().getName(crypto));
		
		buySellBtn.setStyle("-fx-background-color:  #00f86b;");
		buySellBtn.setText("Buy");
		
		amountField.setText("");
		totalField.setText("");
		
		balance = ClientLogic.getInstance().getBalance();
		balance = round(balance, 3);
		balanceLbl.setText("$"+balance);
	}
	
	private void setAmount(Double val) {		
		if(buy) {
			Double amount = val / price;
			
			amount = round(amount, 5);
			
			BigDecimal d = new BigDecimal(amount);
			
			d = d.setScale(5, RoundingMode.DOWN); 
			
			amountField.setText(d.toPlainString());
		}
		else {
			val = round(val, 5);
			
			BigDecimal d = new BigDecimal(val);
			
			d = d.setScale(5, RoundingMode.DOWN); 
			
			amountField.setText(d.toPlainString());
		}
	}
	
	private void setTotal(double percent) {
		if(buy) {
			Double val = balance * percent;		
			
			val = round(val, 3);
			
			BigDecimal d = new BigDecimal(val);
			
			d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
			
			totalField.setText(d.toPlainString());
			
			setAmount(val);
		}
		else {
			Double val = balance * percent;
			
			Double totalUSD = val * price;
			
			totalUSD = round(totalUSD, 3);
			
			BigDecimal d = new BigDecimal(totalUSD);
			
			d = d.setScale(3, RoundingMode.DOWN);
			
			totalField.setText(d.toPlainString());
			
			setAmount(val);
		}
	}

	
	@FXML
	public void percent25() {
		setTotal(0.25);
	}
	
	
	@FXML
	public void percent50() {
		setTotal(0.50);
	}
	
	@FXML
	public void percent75() {
		setTotal(0.75);
	}
	
	@FXML
	public void percent100() {
		setTotal(1.00);
	}
	
	public void buySellPressed() throws IOException { //BUY or SELL is pressed. 
		if(amountField.getText().isEmpty()){
			errorLbl.setText("Please specify the amount you want to transact.");
			return;
		}
		String amountStr = amountField.getText();
		try {
			Double amount = Double.valueOf(amountStr);
			if(amount <= 0) {
				errorLbl.setText("Please specify an amount greater than 0.");
				return;
			}			
			Double total = Double.valueOf(totalField.getText());
			if(total>balance && buy){
				errorLbl.setText("Insufficient funds. Please deposit.");
				return;
			}
			if(amount>balance && !buy) {
				errorLbl.setText("Insufficient funds. Please deposit.");
				return;
			}
			
			if(total<10) {
				errorLbl.setText("Minimum order value is 10 USD.");
				return;
			}
				
			
			MsgDialog.getInstance().showPendingDialog();
			if(buy) {
				ClientLogic.getInstance().transaction(amount, total, crypto, Protocol.BUY, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString());
				ClientLogic.getInstance().setTemporaryTransaction(amount, total, crypto, Protocol.BUY, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString(), null);
			}
			else {
				ClientLogic.getInstance().transaction(amount, total, crypto, Protocol.SELL, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString());
				ClientLogic.getInstance().setTemporaryTransaction(amount, total, crypto, Protocol.SELL, java.time.LocalDate.now().toString() + " " + java.time.LocalTime.now().toString(), null);
			}					
			errorLbl.setText("");
		}
		catch(NumberFormatException e) {
			errorLbl.setText("Please specify the amount in a correct format.");
		}
	}
	
	public void updateBalance() {
		if(buy) {
			balance = ClientLogic.getInstance().getBalance();
			balance = round(balance, 3);
			
			BigDecimal d = new BigDecimal(balance);
			
			d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
			
			balanceLbl.setText("$"+d.toPlainString());
		}
		else {
			balance = ClientLogic.getInstance().getAmountOwned(crypto);
			balance = round(balance, 5);
			BigDecimal d = new BigDecimal(balance);
			
			d = d.setScale(5, RoundingMode.DOWN); 
			
			balanceLbl.setText(d.toPlainString() + " " + crypto);
		}
	}
	private void openLink(String url) throws IOException, URISyntaxException {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI(url));
		}
	}
	
	@FXML
	private void openWebsite() throws IOException, URISyntaxException {
		openLink(AboutCrypto.getInstance().getWebsite(crypto));
	}
	
	@FXML
	private void openWhitepaper() throws IOException, URISyntaxException {
		openLink(AboutCrypto.getInstance().getWhitepaper(crypto));
	}
	
	@FXML
	public void showMarketCapInfo() throws IOException {
		MsgDialog.getInstance().showInfo(0);
	}
	
	@FXML
	public void showCirculatingSupplyInfo() throws IOException {
		MsgDialog.getInstance().showInfo(1);
	}
	
	@FXML
	public void showVolumeInfo() throws IOException {
		MsgDialog.getInstance().showInfo(2);
	}
	
	@FXML
	public void openDeposit() throws IOException {
		DepositDialog.getInstance().open();
	}
	
	@FXML
	public void openMyAccount() throws IOException {
		MyAccountDialog.getInstance().open();
	}

	@FXML
	public void setHomePage(){
		try {
			SceneHandler.getInstance().goToHome();
			stopTasks();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@FXML
	public void goToPortfolio() throws IOException {
		ClientLogic.getInstance().updatePortfolioValue();
		SceneHandler.getInstance().setPortfolioScene();
		ClientView.getInstance().assetPercentageChanges();
		ClientView.getInstance().updatePieChart();
		
		ClientView.getInstance().updateOrder();
		
		stopTasks();
	}
	
	@FXML
	public void goToAllAssets() throws Exception {
		SceneHandler.getInstance().setAllAssetsScene();
		ClientView.getInstance().updatePercentageChanges();
		ClientView.getInstance().setAssetStats();
		
		stopTasks();
	}

	private void stopTasks() {
		livePriceTask.cancel();
		livePrice.stop();
	}
	
	@FXML 
	public void logout() throws IOException {
		ClientLogic.getInstance().resetClientLogic();
	}
}


