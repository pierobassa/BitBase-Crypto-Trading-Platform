package application.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.ClientLogic;
import application.model.Prices;
import application.net.common.SupportedAssets;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.KeyValue;

public class AssetController implements Initializable{

	@FXML
    private Pane assetPane;

    @FXML
    private ImageView icon;

    @FXML
    private Label name;

    @FXML
    private Label priceLbl;

    @FXML
    private Label change;

    @FXML
    private Label marketcap;

    @FXML
    private Label circulatingSupply;

    @FXML
    private Label volume;
    
    private String crypto;
    
    Double price;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBinds();
	}

	private void setBinds() {
		name.layoutXProperty().bind(assetPane.widthProperty().multiply(0.06523));
		priceLbl.layoutXProperty().bind(assetPane.widthProperty().multiply(0.219));
		change.layoutXProperty().bind(assetPane.widthProperty().multiply(0.3625).add(4));
		marketcap.layoutXProperty().bind(assetPane.widthProperty().multiply(0.5135).add(6));
		circulatingSupply.layoutXProperty().bind(assetPane.widthProperty().multiply(0.6616).add(9));
		volume.layoutXProperty().bind(assetPane.widthProperty().multiply(0.8564).add(13));
	}

	public void setImg(Image image) {
		icon.setImage(image);
	}

	public void setAsset(String key) {
		crypto = key;
		name.setText(SupportedAssets.getInstance().getName(key) + " (" + key + ")");
	}

	public void setPrice(Double priceIn) {
		Double previousPrice = price;
		price = priceIn;
		priceLbl.setText("$"+priceIn);	
		
		if(previousPrice == null)
			return;
		
		if(price>previousPrice) { //Faded color effect. Green if prices is greater than previous. Red otherwise.
			priceLbl.setText("$"+priceIn);	
	     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(priceLbl.textFillProperty(), Color.rgb(22, 199, 132))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(priceLbl.textFillProperty(), Color.WHITE))
	        );
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		else {
			priceLbl.setText("$"+priceIn);	
		     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(priceLbl.textFillProperty(), Color.rgb(234, 57, 67))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(priceLbl.textFillProperty(), Color.WHITE))
	        );
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		
		if(ClientLogic.getInstance().getPrices30DayReady())
			percentageChange();
	}
	
	public void initValues() {
		price = Prices.getInstance().getCryptoCurrentPrice(crypto);
		if(price == null)
			return;
		priceLbl.setText("$"+price);	
		if(ClientLogic.getInstance().getPrices30DayReady())
			percentageChange();
		
		if(ClientLogic.getInstance().allReady())
			setStats();
	}
	
	public void setStats() {
		Double marketCapDbl = Prices.getInstance().getCryptoStats(crypto).getMarket_cap();
		Double circulatingSupplyDbl = Prices.getInstance().getCryptoStats(crypto).getCirculating_supply();
		Double volumeDbl = Prices.getInstance().getCryptoStats(crypto).getVolume_24h();
		
		marketCapDbl = marketCapDbl / Math.pow(10, 9);
		BigDecimal d = new BigDecimal(marketCapDbl);
		d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
		marketcap.setText("$"+d.toPlainString()+ " B");
		
		circulatingSupplyDbl = circulatingSupplyDbl / Math.pow(10, 6);
		d = new BigDecimal(circulatingSupplyDbl);
		d = d.setScale(4, RoundingMode.DOWN); //Otherwise there are too many decimal units
		circulatingSupply.setText(d.toPlainString() + " M " + crypto);
		

		volumeDbl = volumeDbl / Math.pow(10, 9);
		d = new BigDecimal(volumeDbl);
		d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
		volume.setText("$"+d.toPlainString() + " B");
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	public void percentageChange() {
		Double yesterdayPrice = Prices.getInstance().getCryptoPrices30Days(crypto).elementAt(0).getPrice();
		if(yesterdayPrice == null || price == null) //30 day prices not set yet
			return;
		if(price>=yesterdayPrice) {
			change.setStyle("-fx-text-fill:  #16c784;");
			Double percentageChange = 100 - (yesterdayPrice*100 / price);
			percentageChange = round(percentageChange, 2);
			change.setText("+"+percentageChange+"%");
		}
		else {
			change.setStyle("-fx-text-fill:  #ea3943;");
			Double percentageChange = (yesterdayPrice*100 / price) - 100;
			percentageChange = round(percentageChange, 2);
			change.setText("-"+percentageChange+"%");
		}
	}
}
