package application.model;

public class PriceDate { //Pairs a price with a date
	private String date;
	private Double price;
	
	public PriceDate(String date, Double price) {
		this.date = date;
		this.price = price;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
