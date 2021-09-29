package application.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import application.model.ClientLogic;
import application.model.Prices;
import application.net.common.SupportedAssets;
import application.net.common.Protocol;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SingleAssetController implements Initializable {

	@FXML
	private Pane pane;

	@FXML
	private ImageView icon;

	@FXML
	private Label name;

	@FXML
	private Label currentPrice;

	@FXML
	private Label change;

	@FXML
	private Label holdingValue;

	@FXML
	private Label holdingQuantity;

	@FXML
	private Label PnL;

	@FXML
	private Label PnLPercentage;
	
	private String asset;
	private Double amount;
	private Double holdingValueDbl;
	private Double price;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBinds();
		initValues();
	}

	private void initValues() {
		PnLPercentage.setText(null);
	}

	private void setBinds() {
		PnL.layoutXProperty().bind(pane.widthProperty().multiply(0.855).add(5));
		PnLPercentage.layoutXProperty().bind(pane.widthProperty().multiply(0.855).add(8));
		holdingValue.layoutXProperty().bind(pane.widthProperty().multiply(0.679).add(5));
		holdingQuantity.layoutXProperty().bind(pane.widthProperty().multiply(0.679).add(5));
		change.layoutXProperty().bind(pane.widthProperty().multiply(0.494));
		currentPrice.layoutXProperty().bind(pane.widthProperty().multiply(0.311));
		icon.layoutXProperty().bind(pane.widthProperty().multiply(0.0177));
		name.layoutXProperty().bind(icon.layoutXProperty().add(icon.fitWidthProperty()).add(55));
	}
	
	public void setImg(Image img) {
		Image image = img;
	    icon.setImage(image);	
	}
	
	public void setAsset(String asset) {
		this.asset = asset;
	}
	
	public void setPrice(Double priceIn) {
		Double previousValue = price;
		price = priceIn;
		
		if(previousValue==null) {
			currentPrice.setText("$"+price);	
		}
		else if(price>previousValue) {
			currentPrice.setText("$"+price);	
	     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(currentPrice.textFillProperty(), Color.rgb(22, 199, 132))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(currentPrice.textFillProperty(), Color.WHITE))
	        );
	       
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		else if(price<previousValue){
			currentPrice.setText("$"+price);	
		     
	        Timeline timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0), new KeyValue(currentPrice.textFillProperty(), Color.rgb(234, 57, 67))),
	            new KeyFrame(Duration.seconds(2), new KeyValue(currentPrice.textFillProperty(), Color.WHITE))
	        );
	       
	        timeline.setCycleCount(1);
	        timeline.play();
		}
		else {
			currentPrice.setText("$"+price);	
		}
		
		
		updateholdingValue();
		
		if(asset == Protocol.BTC)
			ClientLogic.getInstance().updatePortfolioValue();
		
		if(asset != Protocol.USD && ClientLogic.getInstance().getPrices30DayReady())
			percentageChange();
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public void percentageChange() { 
		Double yesterdayPrice = Prices.getInstance().getCryptoPrices30Days(asset).elementAt(0).getPrice();
		if(yesterdayPrice == null) //30 day prices not set yet
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
	
	public void updateholdingValue() {
		if(amount == null) //If amount is not set yet
			return;
		
		holdingValueDbl = price * amount;
		
		BigDecimal d = new BigDecimal(holdingValueDbl);
		
		d = d.setScale(3, RoundingMode.DOWN); //Otherwise there are too many decimal units
		
		holdingValue.setText("$" + d.toPlainString());
		
		
	}
	
	public Double getHoldingLblValue() {
		return Double.valueOf(holdingValue.getText().substring(1));
	}
	
	public void updatePnL(Double spent) { 
		
		Double PnLdbl;
		
		if(holdingValueDbl==null)
			return;
		if(spent>=0) 
			PnLdbl = holdingValueDbl - spent;
		else 
			PnLdbl = holdingValueDbl + (spent*=-1);
		
		
		if(holdingValueDbl<1) {
			PnL.setText("+ $0.00");
			PnLPercentage.setText(null);
			return;
		}
		
		BigDecimal d;
		if(PnLdbl<0) {
			PnLdbl *= -1;
			d = new BigDecimal(PnLdbl);
			d = d.setScale(2, RoundingMode.DOWN);
			PnL.setText("- $"+d.toPlainString());
			PnLPercentage.setStyle("-fx-text-fill: #ea3943;");
			Double pnlChange = (price*ClientLogic.getInstance().getAmountOwned(asset) *100 / spent) - 100;
			if(pnlChange.equals(0.0) || pnlChange.isInfinite() || pnlChange.isNaN())
				return;
			d = new BigDecimal(pnlChange);
			d = d.setScale(2, RoundingMode.DOWN);
			PnLPercentage.setText(d.toPlainString()+"%");
		}
		else {
			d = new BigDecimal(PnLdbl);
			d = d.setScale(2, RoundingMode.DOWN);
			PnL.setText("+ $"+d.toPlainString());
			PnLPercentage.setStyle("-fx-text-fill: #16c784;");
			Double pnlChange = (price*ClientLogic.getInstance().getAmountOwned(asset)*100 / spent) - 100;
			if(pnlChange.equals(0.0) || pnlChange.isInfinite() || pnlChange.isNaN())
				return;
			d = new BigDecimal(pnlChange);
			d = d.setScale(2, RoundingMode.DOWN);
			PnLPercentage.setText("+" + d.toPlainString()+"%");
		}
		
		
	}

	public void setName() {
		name.setText(SupportedAssets.getInstance().getName(asset) + " (" + asset + ")");
		
	}

	public void setValues(Double amount) {
		this.amount = amount;
		
		BigDecimal d = new BigDecimal(amount);
		
		d = d.setScale(5, RoundingMode.DOWN); //Otherwise there are too many decimal units
		
		holdingQuantity.setText(d.toPlainString() + " " + asset);
		
		
	}
	
	public Double getHoldingValue() {
		return holdingValueDbl;
	}

	public void setBalance() {
		Double balance = ClientLogic.getInstance().getBalance();
		if(balance == null) //If balance is not set yet
			return;
		BigDecimal d = new BigDecimal(balance);
		
		d = d.setScale(3, RoundingMode.DOWN); 
		
		holdingValueDbl = balance;
		holdingValue.setText("$"+d.toPlainString());
		holdingQuantity.setText(d.toPlainString() + " USD");
	}
	
	public void noUSDValues() {
		PnL.setText(null);
		PnLPercentage.setText(null);
		change.setText(null);
	}

	public Double getPnL() {
		if(asset!=Protocol.USD) {
			String pnl = (PnL.getText().substring(0, PnL.getText().length())).replace(" $", "");
			Double pnlDbl = Double.valueOf(pnl);
			return pnlDbl;
		}
		return null;
	}
	
	public Double getPnLChange() {		
		if(PnLPercentage.getText() == null)
			return 0.0;
		String perc = PnLPercentage.getText().substring(0, PnLPercentage.getText().length()-1);
		Double changeDbl = Double.valueOf(perc);
		
		return changeDbl;
	}
	
	public String getName() {
		return name.getText();
	}

	public String getAmount() {
		return holdingQuantity.getText();
	}
}
