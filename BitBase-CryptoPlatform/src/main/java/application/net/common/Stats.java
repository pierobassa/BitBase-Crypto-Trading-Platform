package application.net.common;

import java.io.Serializable;

public class Stats implements Serializable{ //Asset statistics = market cap, circulating supply and 24 hour volume
	private static final long serialVersionUID = -1052059627110850150L;
	
	private Double market_cap;
	private Double circulating_supply;
	private Double volume_24h;
	
	public Stats(Double market_cap, Double circulating_supply, Double volume_24h) {
		this.market_cap = market_cap;
		this.circulating_supply = circulating_supply;
		this.volume_24h = volume_24h;
	}
	
	public Double getMarket_cap() {
		return market_cap;
	}
	public void setMarket_cap(Double market_cap) {
		this.market_cap = market_cap;
	}
	public Double getCirculating_supply() {
		return circulating_supply;
	}
	public void setCirculating_supply(Double circulating_supply) {
		this.circulating_supply = circulating_supply;
	}
	public Double getVolume_24h() {
		return volume_24h;
	}
	public void setVolume_24h(Double volume_24h) {
		this.volume_24h = volume_24h;
	}
	
	@Override
	public String toString() {
		return market_cap + " " + circulating_supply + " " + volume_24h;
	}
}
