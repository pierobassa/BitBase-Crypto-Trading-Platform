package application.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Vector;
import application.ClientView;
import application.DepositDialog;
import application.MyAccountDialog;
import application.SceneHandler;
import application.model.ClientLogic;
import application.model.ColorCoding;
import application.model.News;
import application.model.PriceDate;
import application.net.common.Protocol;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class ComparatorePrices implements Comparator<PriceDate>{

	@Override
	public int compare(PriceDate o1, PriceDate o2) {
		
		return o1.getPrice().compareTo(o2.getPrice());
	}
	
}

public class HomeController implements Initializable{
	
	@FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane menuPane;

    @FXML
    private ImageView homeIcon;

    @FXML
    private ImageView portfolioIcon;

    @FXML
    private ImageView tradeIcon;

    @FXML
    private ImageView homelogoImg;

    @FXML
    private Pane logoutPane;

    @FXML
    private ImageView logoutIcon;

    @FXML
    private Pane welcomePane;

    @FXML
    private Pane myAccountPane;

    @FXML
    private ImageView accountIcon;

    @FXML
    private Pane buySellBtn;

    @FXML
    private ImageView newsImg;

    @FXML
    private Pane bnbPane;

    @FXML
    private LineChart<String, Number> bnbChart;

    @FXML
    private NumberAxis bnbYaxis;

    @FXML
    private ImageView bnbIcon;

    @FXML
    private Label bnbPriceLbl;

    @FXML
    private Pane dotPane;

    @FXML
    private LineChart<String, Number> dotChart;

    @FXML
    private NumberAxis dotYaxis;

    @FXML
    private ImageView dotIcon;

    @FXML
    private Label dotPriceLbl;

    @FXML
    private Pane adaPane;

    @FXML
    private LineChart<String, Number> adaChart;

    @FXML
    private NumberAxis adaYaxis;

    @FXML
    private ImageView adaIcon;

    @FXML
    private Label adaPriceLbl;

    @FXML
    private Pane ltcPane;

    @FXML
    private LineChart<String, Number> ltcChart;

    @FXML
    private NumberAxis ltcYaxis;

    @FXML
    private ImageView ltcIcon;

    @FXML
    private Label ltcPriceLbl;

    @FXML
    private Pane ethPane;

    @FXML
    private LineChart<String, Number> ethChart;

    @FXML
    private NumberAxis ethYaxis;

    @FXML
    private ImageView ethIcon;

    @FXML
    private Label ethPriceLbl;

    @FXML
    private Pane btcPane;

    @FXML
    private LineChart<String, Number> btcChart;

    @FXML
    private NumberAxis btcYaxis;

    @FXML
    private ImageView btcIcon;

    @FXML
    private Label btcPriceLbl;
    
    @FXML
    private Pane newsPane;

    @FXML
    private Label newsLbl;
    
    @FXML
    private Label newsTitle;

    @FXML
    private Label allNewsLbl;
    
    @FXML
    private Label greetingsLbl;
    
    @FXML
    private Circle firstNews;

    @FXML
    private Circle secondNews;

    @FXML
    private Circle thirdNews;
    
    private Vector<News> news;
    
    Timeline newsChanger;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 String username = ClientLogic.getInstance().getUsername();
		 greetingsLbl.setText("Greetings! Great to have you here " + username + "!");
		 setImages();
		 setBinds();  
		 setInitColoring();
		 labelInits();
	}   
	
	private void labelInits() {
		 newsTitle.setText("");
	     newsLbl.setText("");
	     allNewsLbl.setText("");
	}
	
	

	private void setInitColoring() {
		 btcPriceLbl.setStyle("-fx-text-fill:  #ffb254;");
	     btcPriceLbl.setText("");
	     
	     ethPriceLbl.setStyle("-fx-text-fill:  #a9bafc;");
	     ethPriceLbl.setText("");
	     
	     bnbPriceLbl.setStyle("-fx-text-fill:  #ffd05e;");
	     bnbPriceLbl.setText("");
	     
	     adaPriceLbl.setStyle("-fx-text-fill:  #32d9d9;");
	     adaPriceLbl.setText("");
	     
	     dotPriceLbl.setStyle("-fx-text-fill:  #ff1c94;");
	     dotPriceLbl.setText("");
	     
	     ltcPriceLbl.setStyle("-fx-text-fill:  #f2f2f2;");
	     ltcPriceLbl.setText("");
	}

	private void setBinds() {
		 myAccountPane.layoutXProperty().bind(welcomePane.widthProperty().subtract(180));
	     buySellBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(370));
	     logoutPane.layoutYProperty().bind(menuPane.heightProperty().subtract(70));	
	     
	     btcPriceLbl.layoutXProperty().bind(btcPane.widthProperty().subtract(125));
	     btcChart.prefHeightProperty().bind(btcPane.heightProperty().subtract(50));
	     btcChart.prefWidthProperty().bind(btcPane.widthProperty().subtract(10));
	     
	     ethPriceLbl.layoutXProperty().bind(ethPane.widthProperty().subtract(114));
	     ethChart.prefHeightProperty().bind(ethPane.heightProperty().subtract(50));
	     ethChart.prefWidthProperty().bind(ethPane.widthProperty().subtract(10));
	     
	     bnbPriceLbl.layoutXProperty().bind(bnbPane.widthProperty().subtract(107));
	     bnbChart.prefHeightProperty().bind(bnbPane.heightProperty().subtract(50));
	     bnbChart.prefWidthProperty().bind(bnbPane.widthProperty().subtract(10));
	     
	     adaPriceLbl.layoutXProperty().bind(adaPane.widthProperty().subtract(99));
	     adaChart.prefHeightProperty().bind(adaPane.heightProperty().subtract(50));
	     adaChart.prefWidthProperty().bind(adaPane.widthProperty().subtract(10));
	     
	     dotPriceLbl.layoutXProperty().bind(adaPane.widthProperty().subtract(103));
	     dotChart.prefHeightProperty().bind(adaPane.heightProperty().subtract(50));
	     dotChart.prefWidthProperty().bind(adaPane.widthProperty().subtract(10));
	     
	     ltcPriceLbl.layoutXProperty().bind(adaPane.widthProperty().subtract(107));
	     ltcChart.prefHeightProperty().bind(adaPane.heightProperty().subtract(50));
	     ltcChart.prefWidthProperty().bind(adaPane.widthProperty().subtract(10));
	     
	     newsLbl.prefWidthProperty().bind(newsPane.widthProperty().subtract(300));
	     firstNews.layoutXProperty().bind(newsPane.widthProperty().subtract(91));
	     secondNews.layoutXProperty().bind(newsPane.widthProperty().subtract(60));
	     thirdNews.layoutXProperty().bind(newsPane.widthProperty().subtract(29));
	}

	private void setImages() {
		 Image image = new Image(getClass().getResourceAsStream("/images/homepagelogo.png"));
	     homelogoImg.setImage(image);		
	        
	     image = new Image(getClass().getResourceAsStream("/images/news.png"));
	     newsImg.setImage(image);	
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/home-icon.png"));
	     homeIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/chart-icon.png"));
	     portfolioIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/trade-icon.png"));
	     tradeIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/user-icon.png"));
	     accountIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/logout-icon.png"));
	     logoutIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/bitcoin-icon.png"));
	     btcIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/ethereum-icon.png"));
	     ethIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/binance-icon.png"));
	     bnbIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/cardano-icon.png"));
	     adaIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/polkadot-icon.png"));
	     dotIcon.setImage(image);
	     
	     image = new Image(getClass().getResourceAsStream("/images/icons/litecoin-icon.png"));
	     ltcIcon.setImage(image);
	}
	
	public void setUsername(String newUsername) {
		 greetingsLbl.setText("Greetings! Great to have you here " + newUsername + "!");
	}
	
	public void setBTCprice(Double btcPrice) {
		if(!btcPriceLbl.getText().isEmpty()) {
			String priceNow = btcPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
			Double dblPriceNow = Double.valueOf(priceNow);                					
				
			if(btcPrice > dblPriceNow)
				btcPriceLbl.setStyle("-fx-text-fill:  #ffb254;");
			else
				btcPriceLbl.setStyle("-fx-text-fill:  #e67e00;");
			
			btcPriceLbl.setText("$"+ String.valueOf(btcPrice));
		}
		else
			btcPriceLbl.setText("$"+ String.valueOf(btcPrice));
	}
	
	public void setETHprice(Double price) {
		if(!ethPriceLbl.getText().isEmpty()) {
			String priceNow = ethPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
				
			Double dblPriceNow = Double.valueOf(priceNow);                					
					
				
				
			if(price > dblPriceNow)
				ethPriceLbl.setStyle("-fx-text-fill:  #a9bafc;");
			else
				ethPriceLbl.setStyle("-fx-text-fill:  #5c77db;");
				
			ethPriceLbl.setText("$"+ String.valueOf(price));
		}
		else
			ethPriceLbl.setText("$"+ String.valueOf(price));
	}
	
	public void setBNBprice(Double price) {
		if(!bnbPriceLbl.getText().isEmpty()) {
			String priceNow = bnbPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
				
			Double dblPriceNow = Double.valueOf(priceNow);                					
					
				
				
			if(price > dblPriceNow)
				bnbPriceLbl.setStyle("-fx-text-fill:  #ffd05e;");
			else
				bnbPriceLbl.setStyle("-fx-text-fill:  #f2b92e;");
				
			bnbPriceLbl.setText("$"+ String.valueOf(price));
		}
		else
			bnbPriceLbl.setText("$"+ String.valueOf(price));
	}
	
	public void setADAprice(Double price) {
		if(!adaPriceLbl.getText().isEmpty()) {
			String priceNow = adaPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
				
			Double dblPriceNow = Double.valueOf(priceNow);                					
					
				
				
			if(price > dblPriceNow)
				adaPriceLbl.setStyle("-fx-text-fill:  #32d9d9;");
			else
				adaPriceLbl.setStyle("-fx-text-fill:  #0dbfbf;");
				
			adaPriceLbl.setText("$"+ String.valueOf(price));	
		}
		else
			adaPriceLbl.setText("$"+ String.valueOf(price));	
	}
	
	public void setDOTprice(Double price) {
		if(!dotPriceLbl.getText().isEmpty()) {
			String priceNow = dotPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
				
			Double dblPriceNow = Double.valueOf(priceNow);                					
					
				
				
			if(price > dblPriceNow)
				dotPriceLbl.setStyle("-fx-text-fill:  #ff1c94;");
			else
				dotPriceLbl.setStyle("-fx-text-fill:  #d1006f;");
				
			dotPriceLbl.setText("$"+ String.valueOf(price));
		}
		else
			dotPriceLbl.setText("$"+ String.valueOf(price));
	}
	
	public void setLTCprice(Double price) {
		if(!ltcPriceLbl.getText().isEmpty()) {
			String priceNow = ltcPriceLbl.getText();
			priceNow = priceNow.replace("$", "");
				
			Double dblPriceNow = Double.valueOf(priceNow);                					
					
				
				
			if(price > dblPriceNow)
				ltcPriceLbl.setStyle("-fx-text-fill:  #f2f2f2;");
			else
				ltcPriceLbl.setStyle("-fx-text-fill:  #c9c9c9;");
				
			ltcPriceLbl.setText("$"+ String.valueOf(price));
		}
		else
			ltcPriceLbl.setText("$"+ String.valueOf(price));
	}

	public void initBTC(Vector<PriceDate> prices) { 
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); 
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		series.setName("btc prices");
		
		
		prices.sort(new ComparatorePrices());
		btcYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		btcYaxis.setLowerBound(prices.elementAt(0).getPrice() - prices.elementAt(0).getPrice()*0.01);
		btcYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		

		btcChart.getData().add(series);
	    btcChart.setCreateSymbols(false); 
	    btcChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.BTC))); 
	}
	
	public void initETH(Vector<PriceDate> prices) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); //Una serie per prendere i dati
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		prices.sort(new ComparatorePrices());
		ethYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		ethYaxis.setLowerBound(prices.elementAt(0).getPrice() - prices.elementAt(0).getPrice()*0.01);
		ethYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		

		ethChart.getData().add(series);
	    ethChart.setCreateSymbols(false); 
	    ethChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.ETH))); 
	}
	
	public void initBNB(Vector<PriceDate> prices) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); 
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		prices.sort(new ComparatorePrices());
		bnbYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		bnbYaxis.setLowerBound(prices.elementAt(0).getPrice() - prices.elementAt(0).getPrice()*0.01);
		bnbYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		
		bnbChart.getData().add(series);
		bnbChart.setCreateSymbols(false); 
		bnbChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.BNB))); 
		
	}
	
	public void initADA(Vector<PriceDate> prices) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); 
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		prices.sort(new ComparatorePrices());
		adaYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		adaYaxis.setLowerBound(prices.elementAt(0).getPrice()- prices.elementAt(0).getPrice()*0.01);
		adaYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		
		adaChart.getData().add(series);
		adaChart.setCreateSymbols(false); 
		adaChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.ADA))); 
	}
	
	public void initDOT(Vector<PriceDate> prices) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); 
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		prices.sort(new ComparatorePrices());
		dotYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		dotYaxis.setLowerBound(prices.elementAt(0).getPrice() - prices.elementAt(0).getPrice()*0.01);
		dotYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		
		dotChart.getData().add(series);
		dotChart.setCreateSymbols(false); 
		dotChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.DOT))); 
	}
	
	public void initLTC(Vector<PriceDate> prices) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>(); 
		int cont = 1;
		for(int i=prices.size()-1; i>=0; i--) {
			  series.getData().add(new XYChart.Data<String, Number>(String.valueOf(cont), prices.elementAt(i).getPrice()));
			  cont++;
		}
		
		prices.sort(new ComparatorePrices());
		ltcYaxis.setTickUnit(prices.elementAt(0).getPrice()*0.01);
		ltcYaxis.setLowerBound(prices.elementAt(0).getPrice()-prices.elementAt(0).getPrice()*0.01);
		ltcYaxis.setUpperBound(prices.elementAt(prices.size()-1).getPrice() + prices.elementAt(0).getPrice()*0.01);
		
		ltcChart.getData().add(series);
		ltcChart.setCreateSymbols(false); 
		ltcChart.setStyle(String.format(ColorCoding.getInstance().getChartColor(Protocol.LTC))); 
	}
	
	private void setNews(Vector<News> n) {
		news = n;
	}

	@FXML
	public void firstArticle() {
		firstNews.setFill(Paint.valueOf("#ff69eb"));
		secondNews.setFill(Paint.valueOf("#50568d"));
		thirdNews.setFill(Paint.valueOf("#50568d"));
		if(news.elementAt(0).getTitle().length()<=83)
			newsTitle.setText(news.elementAt(0).getTitle());
		else {
			String title = news.elementAt(0).getTitle();
			title = title.substring(0, 83);
			title += "[...]";
			newsTitle.setText(title);
		}
		newsLbl.setText(news.elementAt(0).getDescription());
	}
	
	@FXML
	public void secondArticle() {
		firstNews.setFill(Paint.valueOf("#50568d"));
		secondNews.setFill(Paint.valueOf("#ff69eb"));
		thirdNews.setFill(Paint.valueOf("#50568d"));
		if(news.elementAt(1).getTitle().length()<=83)
			newsTitle.setText(news.elementAt(1).getTitle());
		else {
			String title = news.elementAt(1).getTitle();
			title = title.substring(0, 83);
			title += "[...]";
			newsTitle.setText(title);
		}
		newsLbl.setText(news.elementAt(1).getDescription());
	}
	
	@FXML
	public void thirdArticle() {
		firstNews.setFill(Paint.valueOf("#50568d"));
		secondNews.setFill(Paint.valueOf("#50568d"));
		thirdNews.setFill(Paint.valueOf("#ff69eb"));
		if(news.elementAt(2).getTitle().length()<=83)
			newsTitle.setText(news.elementAt(2).getTitle());
		else {
			String title = news.elementAt(2).getTitle();
			title = title.substring(0, 83);
			title += "[...]";
			newsTitle.setText(title);
		}
		newsLbl.setText(news.elementAt(2).getDescription());
	}
	
	@FXML 
	public void openNews() throws IOException, URISyntaxException {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI("https://cointelegraph.com"));
		}
	}
	

	public void setNewsHome(Vector<News> news) {
		setNews(news);

		allNewsLbl.setText("Click to open all articles on your browser");
		
		if(news.elementAt(0).getTitle().length()<=78)
			newsTitle.setText(news.elementAt(0).getTitle());
		else {
			String title = news.elementAt(0).getTitle();
			title = title.substring(0, 78);
			title += "[...]";
			newsTitle.setText(title);
		}
		newsLbl.setText(news.elementAt(0).getDescription());
		firstArticle();
	}
	
	public void playNewsTransition() {
		newsChanger = new Timeline(new KeyFrame(Duration.seconds(20), new EventHandler<ActionEvent>() {
			private int i = 0;
    	 	@Override
    	 	public void handle(ActionEvent event) {
    	 		switch(i) {
	    	 		case 0:{
	    	 			firstArticle();
	    	 			break;
	    	 		}
	    	 		case 1:{
	    	 			secondArticle();
	    	 			break;
	    	 		}
	    	 		case 2:{
	    	 			thirdArticle();
	    	 			break;
	    	 		}
    	 		}
    	 		i++;
    	 		if(i>2)
    	 			i=0;
    	 	}	    	 	
		}));
		newsChanger.setCycleCount(Timeline.INDEFINITE);
		newsChanger.play();
	}
	
	public void stopNewsChanger() {
		if(newsChanger!=null)
			newsChanger.stop();
	}

	
	@FXML
	public void setBitcoinPage() {
		try {
			
			SceneHandler.getInstance().setCryptoScene(Protocol.BTC);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@FXML
	public void setEthereumPage() {
		try {
			
			
			SceneHandler.getInstance().setCryptoScene(Protocol.ETH);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@FXML
	public void setBinancePage() {
		try {
			
			
			SceneHandler.getInstance().setCryptoScene(Protocol.BNB);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@FXML
	public void setCardanoPage() {
		try {
			
			
			SceneHandler.getInstance().setCryptoScene(Protocol.ADA);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@FXML
	public void setPolkadotPage() {
		try {
			
			
			SceneHandler.getInstance().setCryptoScene(Protocol.DOT);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@FXML
	public void setLitecoinPage() {
		try {
			
			
			SceneHandler.getInstance().setCryptoScene(Protocol.LTC);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
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
	public void goToPortfolio() throws IOException {
		ClientLogic.getInstance().updatePortfolioValue();
		SceneHandler.getInstance().setPortfolioScene();
		ClientView.getInstance().assetPercentageChanges();
		ClientView.getInstance().updatePieChart();
		
		ClientView.getInstance().updateOrder();
	}
	
	@FXML
	public void goToAllAssets() throws Exception {
		SceneHandler.getInstance().setAllAssetsScene();
		ClientView.getInstance().updatePercentageChanges();
		ClientView.getInstance().setAssetStats();
	}
	
	@FXML 
	public void logout() throws IOException {
		ClientLogic.getInstance().resetClientLogic();
	}

}