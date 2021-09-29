package application.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import application.ClientView;
import application.DepositDialog;
import application.MsgDialog;
import application.MyAccountDialog;
import application.SceneHandler;
import application.SendWithdrawDialog;
import application.export.PortfolioPDF;
import application.export.TransactionsPDF;
import application.model.ClientLogic;
import application.model.ColorCoding;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import application.net.common.Transaction;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

public class PortfolioController implements Initializable{

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
    private Pane myAccountBtn;

    @FXML
    private ImageView accountIcon;

    @FXML
    private Pane depositBtn;

    @FXML
    private Pane sendWithdrawbtn;

    @FXML
    private Label portfolioValue;

    @FXML
    private Pane percentageValuePane;

    @FXML
    private Label percentage24hValue;

    @FXML
    private Label lbl24h;

    @FXML
    private Pane portfolioPane;

    @FXML
    private Label assetNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label changeLbl;

    @FXML
    private Label holdingsLbl;

    @FXML
    private Label PnLlbl;

    @FXML
    private ScrollPane scrollPanePortfolio;

    @FXML
    private VBox vBoxPortfolio;

    @FXML
    private Pane statsPane;

    @FXML
    private Pane pieChartPane;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label noAssetsOwnedLbl;

    @FXML
    private Pane allTimePnLPane;

    @FXML
    private Label allTimePnLLbl;

    @FXML
    private Label alltimePnlValue;

    @FXML
    private Pane alltimePercPane;

    @FXML
    private Label percentageAllTimePnL;

    @FXML
    private ImageView allTimePnLicon;

    @FXML
    private Pane exportPortfolioPane;

    @FXML
    private ImageView exportPortfolioIcon;

    @FXML
    private Pane exportTransactionsPane;

    @FXML
    private ImageView exportTransactionsIcon;

    @FXML
    private Pane bestPerformerPane;

    @FXML
    private Label bestPerformerLbl;

    @FXML
    private ImageView bestPerformerAssetIcon;

    @FXML
    private Label bestPerformerAsset;

    @FXML
    private Label bestPerformerValue;

    @FXML
    private Pane bestPerformerPercPane;

    @FXML
    private Label percentageBestPerformer;

    @FXML
    private ImageView bestPerformerIcon;

    @FXML
    private Pane worstPerformerPane;

    @FXML
    private Label worstPerformerLbl;

    @FXML
    private Label worstPerformerValue;

    @FXML
    private Pane worstPerformerPercPane;

    @FXML
    private Label percentageWorstPerformer;

    @FXML
    private ImageView worstPerformerIcon;

    @FXML
    private ImageView worstPerformerAssetIcon;

    @FXML
    private Label worstPerformerAsset;

    @FXML
    private Pane transactionsPane;

    @FXML
    private ScrollPane scrollPaneTransactions;

    @FXML
    private VBox vBoxTransactions;

    @FXML
    private Label recentTransactionsLbl;
	    
	private Double portfolioValueDbl;
	
	private Map<String, Pane> assetPanes = new HashMap<String, Pane>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			addAssets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBinds();		
		setImages();
		initValues();
		
	}

	private void initValues() {
		noAssetsOwnedLbl.setText(null);
		percentageAllTimePnL.setText("0.00%");
		percentageBestPerformer.setText("0.00%");
		percentageWorstPerformer.setText("0.00%");
		alltimePnlValue.setText("+ $0.00");
		worstPerformerValue.setText("+ $0.00");
		bestPerformerValue.setText("+ $0.00");
		
		bestPerformerAsset.setText(null);
		worstPerformerAsset.setText(null);
		bestPerformerAssetIcon.setImage(null);
		worstPerformerAssetIcon.setImage(null);
	}

	private void setImages() {
		Image image = new Image(getClass().getResourceAsStream("/images/homepagelogo.png"));
	    homelogoImg.setImage(image);	
	    
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
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/export-icon.png"));
	    exportPortfolioIcon.setImage(image);
	    exportTransactionsIcon.setImage(image);
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/up-arrow-icon.png"));
	    allTimePnLicon.setImage(image);		
	    bestPerformerIcon.setImage(image);
	    worstPerformerIcon.setImage(image);
	}
	
	
	private void addAssets() throws IOException {	
		Map<String, SingleAssetController> assetControllers = new HashMap<String, SingleAssetController>();
		Map<String, FXMLLoader> assetLoaders = new LinkedHashMap<String, FXMLLoader>(); //To keep order of insertion
		Map<String, Image> assetIcons = new HashMap<String, Image>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			assetLoaders.put(key, new FXMLLoader(getClass().getResource("/application/view/PortfolioSingleCrypto.fxml")));
			assetIcons.put(key, SupportedAssets.getInstance().getSmallIcon(key));
		}
		
		for(var key : assetLoaders.keySet()) {
			assetPanes.put(key, (Pane) assetLoaders.get(key).load());
			assetControllers.put(key, assetLoaders.get(key).getController());
			if(key != Protocol.USD) {
					assetPanes.get(key).setOnMousePressed(new EventHandler<Event>() {
	
					@Override
					public void handle(Event event) {
						try {
							SceneHandler.getInstance().setCryptoScene(key);
						} catch (IOException e) {
							
							e.printStackTrace();
						}			
					}
				});
			}
			else {
				assetPanes.get(Protocol.USD).setCursor(Cursor.DEFAULT);
				assetControllers.get(Protocol.USD).noUSDValues();
			}
			
			ClientView.getInstance().addAssetController(key, assetLoaders.get(key).getController());
			assetControllers.get(key).setImg(assetIcons.get(key));
			assetControllers.get(key).setAsset(key);
			assetControllers.get(key).setName();	
			
			vBoxPortfolio.getChildren().add(assetPanes.get(key));
			vBoxPortfolio.setPrefHeight(vBoxPortfolio.getPrefHeight()+70);
		}
	}
	
	
	public void updateOrder() throws IOException {
		Vector<Pair<String, Double>> assetValues = new Vector<Pair<String,Double>>();
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD)
				assetValues.add(new Pair<String, Double>(key, ClientView.getInstance().getHoldingValue(key)));
		}
		
		assetValues.sort(new Comparator<Pair<String, Double>>() {

			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		vBoxPortfolio.getChildren().clear();
		vBoxPortfolio.setPrefHeight(9);
		
		vBoxPortfolio.getChildren().add(assetPanes.get(Protocol.USD));
		vBoxPortfolio.setPrefHeight(vBoxPortfolio.getPrefHeight()+70);
		
		for(int i=0; i<assetValues.size(); i++) {
			String key = assetValues.elementAt(i).getKey();
			vBoxPortfolio.getChildren().add(assetPanes.get(key));
			vBoxPortfolio.setPrefHeight(vBoxPortfolio.getPrefHeight()+70);
		}
	}

	private void setBinds() {	
		statsPane.prefHeightProperty().bind(transactionsPane.layoutYProperty().subtract(109));	
		transactionsPane.prefWidthProperty().bind(welcomePane.widthProperty().multiply(0.257));
		transactionsPane.prefHeightProperty().bind(menuPane.heightProperty().multiply(0.539));
		portfolioPane.prefWidthProperty().bind(transactionsPane.layoutXProperty().subtract(256));
		portfolioPane.prefHeightProperty().bind(menuPane.heightProperty().multiply(0.539));
		logoutPane.layoutYProperty().bind(menuPane.heightProperty().subtract(70));	
		
		scrollPanePortfolio.prefWidthProperty().bind(portfolioPane.widthProperty());
		scrollPanePortfolio.prefHeightProperty().bind(portfolioPane.heightProperty().subtract(38));
		vBoxPortfolio.prefWidthProperty().bind(scrollPanePortfolio.widthProperty().subtract(15));
		
		PnLlbl.layoutXProperty().bind(portfolioPane.widthProperty().multiply(0.855).subtract(5));
		holdingsLbl.layoutXProperty().bind(portfolioPane.widthProperty().multiply(0.679).subtract(5));
		changeLbl.layoutXProperty().bind(portfolioPane.widthProperty().multiply(0.494).subtract(5));
		priceLbl.layoutXProperty().bind(portfolioPane.widthProperty().multiply(0.311).subtract(5));
		assetNameLbl.layoutXProperty().bind(portfolioPane.widthProperty().multiply(0.0177));
		
		percentageValuePane.layoutXProperty().bind(portfolioValue.widthProperty().add(42));
		percentageValuePane.prefWidthProperty().bind(percentage24hValue.widthProperty().add(13));
		lbl24h.layoutXProperty().bind(percentageValuePane.layoutXProperty().add(percentageValuePane.widthProperty()).add(7));
		
		myAccountBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(180));
		sendWithdrawbtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(370));
		depositBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(560));
		
		
		pieChartPane.prefHeightProperty().bind(statsPane.heightProperty().subtract(35));
		pieChartPane.prefWidthProperty().bind(pieChart.prefWidthProperty());
		
		pieChart.prefHeightProperty().bind(pieChartPane.prefHeightProperty());
		pieChart.prefWidthProperty().bind(pieChart.prefHeightProperty().multiply(1.442));
		
		recentTransactionsLbl.layoutXProperty().bind(transactionsPane.prefWidthProperty().multiply(0.0615));
		scrollPaneTransactions.prefWidthProperty().bind(transactionsPane.prefWidthProperty());
		scrollPaneTransactions.prefHeightProperty().bind(transactionsPane.prefHeightProperty().subtract(38));
		vBoxTransactions.prefWidthProperty().bind(scrollPaneTransactions.prefWidthProperty().subtract(16));
		
		noAssetsOwnedLbl.layoutXProperty().bind(pieChartPane.prefWidthProperty().divide(2).subtract(noAssetsOwnedLbl.widthProperty().divide(2)));
		noAssetsOwnedLbl.layoutYProperty().bind(pieChartPane.prefHeightProperty().divide(2).subtract(noAssetsOwnedLbl.heightProperty().divide(2)));

		allTimePnLPane.layoutXProperty().bind(pieChartPane.layoutXProperty().add(pieChartPane.widthProperty()).add(50));
		allTimePnLPane.layoutYProperty().bind(statsPane.heightProperty().multiply(0.0510));
		allTimePnLPane.prefWidthProperty().bind(statsPane.widthProperty().multiply(0.499));
		allTimePnLPane.prefHeightProperty().bind(statsPane.heightProperty().multiply(0.187));
		
		allTimePnLLbl.layoutXProperty().bind(allTimePnLPane.widthProperty().multiply(0.0410));
		allTimePnLLbl.layoutYProperty().bind(allTimePnLPane.heightProperty().divide(2).subtract(allTimePnLLbl.heightProperty().divide(2)));
		
		alltimePercPane.layoutXProperty().bind(allTimePnLPane.widthProperty().multiply(0.287));
		alltimePercPane.layoutYProperty().bind(allTimePnLPane.heightProperty().divide(2).subtract(alltimePercPane.heightProperty().divide(2)));
		
		alltimePnlValue.layoutXProperty().bind(allTimePnLPane.widthProperty().multiply(0.524));
		alltimePnlValue.layoutYProperty().bind(allTimePnLPane.heightProperty().divide(2).subtract(alltimePnlValue.heightProperty().divide(2)));
		
		

		bestPerformerPane.layoutXProperty().bind(pieChartPane.layoutXProperty().add(pieChartPane.widthProperty()).add(50));
		bestPerformerPane.layoutYProperty().bind(statsPane.heightProperty().multiply(0.306));
		bestPerformerPane.prefWidthProperty().bind(statsPane.widthProperty().multiply(0.499));
		bestPerformerPane.prefHeightProperty().bind(statsPane.heightProperty().multiply(0.187));
		
		bestPerformerLbl.layoutXProperty().bind(bestPerformerPane.widthProperty().multiply(0.0410));
		bestPerformerLbl.layoutYProperty().bind(bestPerformerPane.heightProperty().divide(2).subtract(bestPerformerLbl.heightProperty().divide(2)));

		bestPerformerPercPane.layoutXProperty().bind(bestPerformerPane.widthProperty().multiply(0.287));
		bestPerformerPercPane.layoutYProperty().bind(bestPerformerPane.heightProperty().divide(2).subtract(bestPerformerPercPane.heightProperty().divide(2)));
		
		bestPerformerValue.layoutXProperty().bind(bestPerformerPane.widthProperty().multiply(0.524));
		bestPerformerValue.layoutYProperty().bind(bestPerformerPane.heightProperty().divide(2).subtract(bestPerformerValue.heightProperty().divide(2)));
		
		bestPerformerAssetIcon.layoutXProperty().bind(bestPerformerPane.widthProperty().multiply(0.718));
		bestPerformerAssetIcon.layoutYProperty().bind(bestPerformerPane.heightProperty().divide(2).subtract(bestPerformerAssetIcon.fitHeightProperty().divide(2)));
		
		bestPerformerAsset.layoutXProperty().bind(bestPerformerAssetIcon.layoutXProperty().add(44));
		bestPerformerAsset.layoutYProperty().bind(bestPerformerPane.heightProperty().divide(2).subtract(bestPerformerAsset.heightProperty().divide(2)));
		
		
		
		worstPerformerPane.layoutXProperty().bind(pieChartPane.layoutXProperty().add(pieChartPane.widthProperty()).add(50));
		worstPerformerPane.layoutYProperty().bind(statsPane.heightProperty().multiply(0.557));
		worstPerformerPane.prefWidthProperty().bind(statsPane.widthProperty().multiply(0.499));
		worstPerformerPane.prefHeightProperty().bind(statsPane.heightProperty().multiply(0.187));
		
		worstPerformerLbl.layoutXProperty().bind(worstPerformerPane.widthProperty().multiply(0.0410));
		worstPerformerLbl.layoutYProperty().bind(worstPerformerPane.heightProperty().divide(2).subtract(worstPerformerLbl.heightProperty().divide(2)));		
		
		worstPerformerPercPane.layoutXProperty().bind(worstPerformerPane.widthProperty().multiply(0.287));
		worstPerformerPercPane.layoutYProperty().bind(worstPerformerPane.heightProperty().divide(2).subtract(worstPerformerPercPane.heightProperty().divide(2)));		
		
		worstPerformerValue.layoutXProperty().bind(worstPerformerPane.widthProperty().multiply(0.524));
		worstPerformerValue.layoutYProperty().bind(worstPerformerPane.heightProperty().divide(2).subtract(worstPerformerValue.heightProperty().divide(2)));		
		
		worstPerformerAssetIcon.layoutXProperty().bind(worstPerformerPane.widthProperty().multiply(0.718));
		worstPerformerAssetIcon.layoutYProperty().bind(worstPerformerPane.heightProperty().divide(2).subtract(worstPerformerAssetIcon.fitHeightProperty().divide(2)));		
		
		worstPerformerAsset.layoutXProperty().bind(worstPerformerAssetIcon.layoutXProperty().add(44));
		worstPerformerAsset.layoutYProperty().bind(worstPerformerPane.heightProperty().divide(2).subtract(worstPerformerAsset.heightProperty().divide(2)));		
		
		
		exportPortfolioPane.layoutXProperty().bind(portfolioPane.widthProperty().subtract(244));
		exportPortfolioPane.layoutYProperty().bind(statsPane.heightProperty().subtract(51));
		exportTransactionsPane.layoutXProperty().bind(statsPane.widthProperty().subtract(238));
		exportTransactionsPane.layoutYProperty().bind(statsPane.heightProperty().subtract(51));
	}
	
	public void setPortfolioValue(Double value) {
		if(value.equals(0.0)) {
			noAssetsOwnedLbl.setText("No assets owned");
		}
		else
			noAssetsOwnedLbl.setText(null);
		
		Double previousValue = portfolioValueDbl;
		
		portfolioValueDbl = value;
		
		BigDecimal d = new BigDecimal(value);
		
		d = d.setScale(2, RoundingMode.DOWN);
		
		if(previousValue==null) {
			portfolioValue.setText("$"+d.toPlainString());
			return;
		}
		
		if(portfolioValueDbl>previousValue) {
			portfolioValue.setText("$"+d.toPlainString());
	     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(portfolioValue.textFillProperty(), Color.rgb(22, 199, 132))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(portfolioValue.textFillProperty(), Color.WHITE))
	        );
	       
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		else if(portfolioValueDbl<previousValue){
			portfolioValue.setText("$"+d.toPlainString());
		     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(portfolioValue.textFillProperty(), Color.rgb(234, 57, 67))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(portfolioValue.textFillProperty(), Color.WHITE))
	        );
	       
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		else {
			portfolioValue.setText("$"+d.toPlainString());
		}
		
		update24hPercentage();
	}
	
	private void update24hPercentage() {
		if(portfolioValueDbl.equals(0.0)) {
			percentageValuePane.setStyle("-fx-background-color:  #16C784;");
			
			percentage24hValue.setText("+0.00%");
			return;
		}
		
		if(!ClientLogic.getInstance().allReady())
			return;
		
		Double portfolioValueWithYesterdayPrices = ClientLogic.getInstance().getPortfolioValueYesterday() + ClientView.getInstance().getAssetController(Protocol.USD).getHoldingValue() ;
		
		Double differencePerc = (portfolioValueDbl*100 / portfolioValueWithYesterdayPrices) - 100;
		
		if(differencePerc>=0) {
			percentageValuePane.setStyle("-fx-background-color:  #16C784;");
			BigDecimal d = new BigDecimal(differencePerc);
			
			d = d.setScale(2, RoundingMode.DOWN);
			
			percentage24hValue.setText("+"+d.toPlainString()+"%");
		}
		else {
			percentageValuePane.setStyle("-fx-background-color:  #EA3943;");
			
			differencePerc = Math.abs(differencePerc);
			
			BigDecimal d = new BigDecimal(differencePerc);
			
			d = d.setScale(2, RoundingMode.DOWN);
			
			percentage24hValue.setText("-"+d.toPlainString()+"%");
		}
	}

	public void updatePieChart(Vector<Pair<String, Double>> holdingValues) {
		pieChart.getData().clear();
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			
		Double others = 0.0;
		for(int i=0; i<holdingValues.size(); i++) {
			if(!holdingValues.get(i).getValue().equals(0.0) && i<4)
				pieChartData.add(new PieChart.Data(holdingValues.get(i).getKey(), holdingValues.get(i).getValue()));
			if(!holdingValues.get(i).getValue().equals(0.0) && i>=4)
				others += holdingValues.get(i).getValue();
		}
		if(!others.equals(0.0))
			pieChartData.add(new PieChart.Data("Other", others));
		
		pieChart.setData(pieChartData);
	
		pieChart.getData().forEach(data -> {
			String percentage = String.format(data.getName() + " %.2f%%", (data.getPieValue() * 100 / portfolioValueDbl));
			Tooltip toolTip = new Tooltip(percentage);
			Tooltip.install(data.getNode(), toolTip);
		});
		
		pieChart.setStyle(String.format("-data-color-1: " + ColorCoding.getInstance().getPrimaryColor(holdingValues.get(0).getKey())) 
				  + " " + String.format("-data-color-2: " + ColorCoding.getInstance().getPrimaryColor(holdingValues.get(1).getKey()))
				  + " " + String.format("-data-color-3: " + ColorCoding.getInstance().getPrimaryColor(holdingValues.get(2).getKey()))
				  + " " + String.format("-data-color-4: " + ColorCoding.getInstance().getPrimaryColor(holdingValues.get(3).getKey()))	 
				  + " " + String.format("-data-color-5: #c0ffba;" )); 
	}

	public void setRecentTransactions(Vector<Transaction> transactions) throws IOException {
		vBoxTransactions.getChildren().clear();
		vBoxTransactions.setPrefHeight(6);
		
		for(int i=0; i<transactions.size(); i++) {
			FXMLLoader loader =  new FXMLLoader(getClass().getResource("/application/view/Transaction.fxml"));
			Pane transaction = (Pane) loader.load();
			
			TransactionController controller = loader.getController();
			String title = "";
			String description = "";
			String imgSrc = "";
			switch(transactions.elementAt(i).getBuy()) {
				case Protocol.BUY:{
					title += "Bought ";
					description += "+";
					imgSrc = "buy-icon.png";
					break;
				}
				case Protocol.SELL:{
					title += "Sold ";
					description += "-";
					imgSrc = "sell-icon.png";
					break;
				}
				case Protocol.DEPOSIT:{
					title += "Deposited ";
					description += "+";
					imgSrc = "deposit-icon.png";
					break;
				}
				case Protocol.WITHDRAW:{
					title += "Withdrew ";
					description += "-";
					imgSrc = "withdraw-icon.png";
					break;
				}
				case Protocol.SEND:{
					if(ClientLogic.getInstance().getUsername().equals(transactions.elementAt(i).getUsername())) {
						title += "Sent ";
						description += "-";
						imgSrc = "send-icon.png";
						break;
					}
					else {
						title += "Received ";
						description += "+";
						imgSrc = "receive-icon.png";
						break;
					}
				}
			}
			if(transactions.elementAt(i).getCrypto() != null) {
				title += SupportedAssets.getInstance().getName(transactions.elementAt(i).getCrypto());
				
			}
			description += "$"+transactions.elementAt(i).getTotal() + " on ";
			
			String dateTime = transactions.elementAt(i).getDate();
			String[] parts = dateTime.split(" ");
			String date = parts[0]; 
			String time = parts[1]; 
			
			String DD = date.substring(8);
			String MM = date.substring(5, 7);
			String YY = date.substring(2, 4);
			date =  DD + "/" + MM + "/" + YY;
			
			time = time.substring(0, 5);
			
			description += date + " " + time;
			
			controller.setValues(title, description, imgSrc);
			
			 
			vBoxTransactions.getChildren().add(transaction);
			vBoxTransactions.setPrefHeight(vBoxTransactions.getPrefHeight() + 56);
		}
	}

	public void updateAllTimePnL(Double totalSpent) {
		Double initialValue = ClientView.getInstance().getAssetController(Protocol.USD).getHoldingValue() + totalSpent;
		if(initialValue.equals(0.0))
			return;
		Double allTimePnL = (portfolioValueDbl * 100 / initialValue) - 100;
		Double difference = portfolioValueDbl - initialValue;
		
		if(allTimePnL>=0) {
			Image image = new Image(getClass().getResourceAsStream("/images/icons/up-arrow-icon.png"));
		    allTimePnLicon.setImage(image);		
		    alltimePercPane.setStyle("-fx-background-color:  #16C784;");
		    
		    BigDecimal d = new BigDecimal(allTimePnL);
			d = d.setScale(2, RoundingMode.DOWN);
		    
		    percentageAllTimePnL.setText(d.toPlainString()+"%");
		    
		    d = new BigDecimal(difference);
		    d = d.setScale(2, RoundingMode.DOWN);
		    
		    alltimePnlValue.setStyle("-fx-text-fill: #16c784;");
		    alltimePnlValue.setText("+ $"+d.toPlainString());		    
		}
		else {
			Image image = new Image(getClass().getResourceAsStream("/images/icons/down-arrow-icon.png"));
		    allTimePnLicon.setImage(image);		
		    alltimePercPane.setStyle("-fx-background-color: #ea3943;");
		    
		    allTimePnL *= -1;
		    difference *= -1;
		    BigDecimal d = new BigDecimal(allTimePnL);
			d = d.setScale(2, RoundingMode.DOWN);
		    
		    percentageAllTimePnL.setText(d.toPlainString()+"%");
		    
		    d = new BigDecimal(difference);
		    d = d.setScale(2, RoundingMode.DOWN);
		    
		    alltimePnlValue.setStyle("-fx-text-fill: #ea3943;");
		    alltimePnlValue.setText("- $"+d.toPlainString());		  
		}
	}

	public void updateBestAndWorstPerferomers(String bestPerformer, String worstPerformer) {
		bestPerformerAsset.setText(SupportedAssets.getInstance().getName(bestPerformer));
		worstPerformerAsset.setText(SupportedAssets.getInstance().getName(worstPerformer));
		bestPerformerAssetIcon.setImage(SupportedAssets.getInstance().getSmallIcon(bestPerformer));
		worstPerformerAssetIcon.setImage(SupportedAssets.getInstance().getSmallIcon(worstPerformer));
		
		
		Double bestPerformerChange = ClientView.getInstance().assetChange(bestPerformer);
		Double worstPerformerChange = ClientView.getInstance().assetChange(worstPerformer);
		
		if(bestPerformerChange >= 0) {
			bestPerformerChange = Math.abs(bestPerformerChange);
			percentageBestPerformer.setText(bestPerformerChange+"%");
			Image image = new Image(getClass().getResourceAsStream("/images/icons/up-arrow-icon.png"));
			bestPerformerIcon.setImage(image);
			bestPerformerPercPane.setStyle("-fx-background-color:  #16C784;");
			
			Double pnl = ClientView.getInstance().assetPnL(bestPerformer);
			pnl = Math.abs(pnl);
			bestPerformerValue.setStyle("-fx-text-fill: #16c784;");
			bestPerformerValue.setText("+ $" + pnl);
		}
		else {
			bestPerformerChange = Math.abs(bestPerformerChange);
			percentageBestPerformer.setText(bestPerformerChange+"%");
			Image image = new Image(getClass().getResourceAsStream("/images/icons/down-arrow-icon.png"));
			bestPerformerIcon.setImage(image);
			bestPerformerPercPane.setStyle("-fx-background-color:  #ea3943;");
			
			Double pnl = ClientView.getInstance().assetPnL(bestPerformer);
			pnl = Math.abs(pnl);
			bestPerformerValue.setStyle("-fx-text-fill: #ea3943;");
			bestPerformerValue.setText("- $" + pnl);
		}
		
		if(worstPerformerChange >= 0) {
			worstPerformerChange = Math.abs(worstPerformerChange);
			percentageWorstPerformer.setText(worstPerformerChange+"%");
			Image image = new Image(getClass().getResourceAsStream("/images/icons/up-arrow-icon.png"));
			worstPerformerIcon.setImage(image);
			worstPerformerPercPane.setStyle("-fx-background-color:  #16C784;");
			
			Double pnl = ClientView.getInstance().assetPnL(worstPerformer);
			pnl = Math.abs(pnl);
			worstPerformerValue.setStyle("-fx-text-fill: #16c784;");
			worstPerformerValue.setText("+ $" + pnl);
		}
		else {
			worstPerformerChange = Math.abs(worstPerformerChange);
			percentageWorstPerformer.setText(worstPerformerChange+"%");
			Image image = new Image(getClass().getResourceAsStream("/images/icons/down-arrow-icon.png"));
			worstPerformerIcon.setImage(image);
			worstPerformerPercPane.setStyle("-fx-background-color:  #ea3943;");
			
			Double pnl = ClientView.getInstance().assetPnL(worstPerformer);
			pnl = Math.abs(pnl);
			worstPerformerValue.setStyle("-fx-text-fill: #ea3943;");
			worstPerformerValue.setText("- $" + pnl);
		}
	}

	public void resetBestAndWorstPerformers() {
		percentageAllTimePnL.setText("0.00%");
		percentageBestPerformer.setText("0.00%");
		percentageWorstPerformer.setText("0.00%");
		alltimePnlValue.setText("+ $0.00");
		worstPerformerValue.setText("+ $0.00");
		bestPerformerValue.setText("+ $0.00");
		
		bestPerformerPercPane.setStyle("-fx-background-color:  #16C784;");
		worstPerformerPercPane.setStyle("-fx-background-color:  #16C784;");
		
		Image image = new Image(getClass().getResourceAsStream("/images/icons/up-arrow-icon.png"));
		bestPerformerIcon.setImage(image);		
		worstPerformerIcon.setImage(image);
		
		bestPerformerAsset.setText(null);
		worstPerformerAsset.setText(null);
		bestPerformerAssetIcon.setImage(null);
		worstPerformerAssetIcon.setImage(null);
	}
	
	@FXML
	public void openSendWithdraw() throws IOException {
		SendWithdrawDialog.getInstance().open();
	}
	
	@FXML
	public void exportPortfolio() throws IOException {
		if(PortfolioPDF.getInstance().exportPortfolioPDF())
			MsgDialog.getInstance().showSuccess("Portfolio exported in the same source folder");
	}
	
	@FXML
	public void exportTransactions() throws IOException  {
		if(TransactionsPDF.getInstance().exportTransactionsPDF())
			MsgDialog.getInstance().showSuccess("Transactions exported in the same source folder");
	}

	@FXML
	public void setHomePage(){
		try {
			SceneHandler.getInstance().goToHome();	
		} catch (Exception e) {
			System.out.println(e);	
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

