package application.controller;

import java.io.IOException;
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
import application.model.ClientLogic;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class AllAssetsController  implements Initializable{

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
    private Label InfoLbl;

    @FXML
    private Pane allAssetsPane;

    @FXML
    private Label assetNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label changeLbl;

    @FXML
    private ScrollPane scrollPaneAssets;

    @FXML
    private VBox vBoxAssets;

    @FXML
    private Label marketCapLbl;

    @FXML
    private Label volumeLbl;

    @FXML
    private Label circulatingSupplyLbl;

    @FXML
    private Pane searchPane;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchImg;
    
    private Map<String, Pane> assetPanes = new HashMap<String, Pane>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBinds();		
		setImages();
		try {
			addAssets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		searchListener();
	}
	

    private void searchListener() {
    	searchField.lengthProperty().addListener(new ChangeListener<Number>() {
			

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { //search field listener to update the viewed assets
				if (newValue.intValue() > oldValue.intValue()) {
					String input = searchField.getText();
                    if(!Character.isLetter(input.charAt(input.length()-1))) //Characters that are not digits are not valid, besides '.'
                    	searchField.setText(searchField.getText().substring(0, searchField.getText().length()-1));
                    else {
                    	vBoxAssets.getChildren().clear();
                    	vBoxAssets.setPrefHeight(5);
                    	try {
							addSearchedAssets(input);
						} catch (IOException e) {
							e.printStackTrace();
						}
                    }
                }
				else if(newValue.intValue() < oldValue.intValue()){
					String input = searchField.getText();
					if(input.length()==0) {
						vBoxAssets.getChildren().clear();
						vBoxAssets.setPrefHeight(5);
						updateOrder();
					}
					else {
						vBoxAssets.getChildren().clear();
                    	vBoxAssets.setPrefHeight(5);
                    	try {
							addSearchedAssets(input);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
    
    private void addSearchedAssets(String input) throws IOException { //Shows assets that satisfy the search field input
		
		input = input.toLowerCase();
		
		Vector<Pair<String, Double>> assetMcaps = new Vector<Pair<String, Double>>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			String asset = SupportedAssets.getInstance().getName(key) + " " + key;
			asset = asset.toLowerCase();
			if(key != Protocol.USD && asset.contains(input)) {
				assetMcaps.add(new Pair<String, Double>(key, Prices.getInstance().getCryptoStats(key).getMarket_cap()));
			}
			
		}
		
		assetMcaps.sort(new Comparator<Pair<String, Double>>() {

			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		for(int i=0; i<assetMcaps.size(); i++) {
			String key = assetMcaps.elementAt(i).getKey();
			vBoxAssets.getChildren().add(assetPanes.get(key));
			vBoxAssets.setPrefHeight(vBoxAssets.getPrefHeight()+70);
		}
	}


	private void addAssets() throws IOException {
    	Map<String, AssetController> assetControllers = new HashMap<String, AssetController>();
		Map<String, FXMLLoader> assetLoaders = new LinkedHashMap<String, FXMLLoader>(); //To keep order of insertion
		Map<String, Image> assetIcons = new HashMap<String, Image>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD) {
				assetLoaders.put(key, new FXMLLoader(getClass().getResource("/application/view/SingleAsset.fxml")));
				assetIcons.put(key, SupportedAssets.getInstance().getSmallIcon(key));
			}
		}
		
		for(var key : assetLoaders.keySet()) {
			assetPanes.put(key, (Pane) assetLoaders.get(key).load());
			assetControllers.put(key, assetLoaders.get(key).getController());
			
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
			
			
			ClientView.getInstance().addSingleAssetController(key, assetLoaders.get(key).getController());
			assetControllers.get(key).setImg(assetIcons.get(key));
			assetControllers.get(key).setAsset(key);
			assetControllers.get(key).initValues();
			
			vBoxAssets.getChildren().add(assetPanes.get(key));
			vBoxAssets.setPrefHeight(vBoxAssets.getPrefHeight()+70);
		}
		
	}
	
	public void updateOrder() { //Orders assets by decreasing market cap
		Vector<Pair<String, Double>> assetMcaps = new Vector<Pair<String, Double>>();
		
		for(var key : SupportedAssets.getInstance().getAssets().keySet()) {
			if(key != Protocol.USD)
				assetMcaps.add(new Pair<String, Double>(key, Prices.getInstance().getCryptoStats(key).getMarket_cap()));
		}
		
		assetMcaps.sort(new Comparator<Pair<String, Double>>() {

			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		vBoxAssets.getChildren().clear();
		vBoxAssets.setPrefHeight(5);
		for(int i=0; i<assetMcaps.size(); i++) {
			String key = assetMcaps.elementAt(i).getKey();
			vBoxAssets.getChildren().add(assetPanes.get(key));
			vBoxAssets.setPrefHeight(vBoxAssets.getPrefHeight()+70);
		}
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
	    
	    image = new Image(getClass().getResourceAsStream("/images/icons/search-icon.png"));
	    searchImg.setImage(image);
	}


	private void setBinds() {
		myAccountBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(180));
	    depositBtn.layoutXProperty().bind(welcomePane.widthProperty().subtract(370));
		scrollPaneAssets.prefWidthProperty().bind(allAssetsPane.widthProperty());
		scrollPaneAssets.prefHeightProperty().bind(allAssetsPane.heightProperty().subtract(99));
		vBoxAssets.prefWidthProperty().bind(scrollPaneAssets.widthProperty().subtract(14));
		logoutPane.layoutYProperty().bind(menuPane.heightProperty().subtract(70));	
		
		searchPane.prefWidthProperty().bind(allAssetsPane.widthProperty().multiply(0.301));
		searchField.prefWidthProperty().bind(searchPane.widthProperty().subtract(48));
		
		assetNameLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.06523));
		priceLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.219));
		changeLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.3625));
		marketCapLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.5135));
		circulatingSupplyLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.6616));
		volumeLbl.layoutXProperty().bind(allAssetsPane.widthProperty().multiply(0.8564));
	}
    
    @FXML
	public void setHomePage() throws IOException{
		try {
			SceneHandler.getInstance().goToHome();
		} catch (Exception e) {
			System.out.println(e);
			MsgDialog.getInstance().showError("error while loading crypto screen");
		}
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
	public void openDeposit() throws IOException {
		DepositDialog.getInstance().open();
	}
    
    @FXML
	public void openMyAccount() throws IOException {
		MyAccountDialog.getInstance().open();
	}
    
    @FXML 
	public void logout() throws IOException {
		ClientLogic.getInstance().resetClientLogic();
	}

}
