package application.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import application.model.PriceDate;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class DataServiceLast30Days extends Service<Vector<PriceDate>>{//Il tipo del service si basa al tipo di ritorno che voglio. Se fa operazioni in background deve essere void

	private  String dati;
	
	public void setDati(String dati) {
		this.dati = dati;
	}
	
	
	@Override
	protected Task<Vector<PriceDate>> createTask() {
		
		return new Task<Vector<PriceDate>>() {

			@Override
			protected Vector<PriceDate> call() throws Exception {
				try {
					Vector<PriceDate> prices = new Vector<PriceDate>();
		
					String url = dati;
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					
					inputLine = in.readLine();
					while(inputLine != null) {
						response.append(inputLine);
						inputLine = in.readLine();
					}
					in.close();
				
					JSONObject jsonResponse = new JSONObject(response.toString());
					JSONArray jsonPrices = jsonResponse.getJSONObject("result").getJSONArray("86400");
					
					for(int i=jsonPrices.length()-3; i>=jsonPrices.length()-32; i--) { //from yesterday to 31 days a go
						Long timestamp = Long.valueOf(jsonPrices.getJSONArray(i).get(0).toString());
						Double price = Double.valueOf(jsonPrices.getJSONArray(i).get(1).toString());
						timestamp *= 1000;
						Timestamp ts = new Timestamp(timestamp);  
			            Date date=new Date(ts.getTime());  
			            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			            prices.add(new PriceDate(dt.format(date).substring(5, 10), price));
					}
					
					return prices;
				}
				catch(Exception e) {
					System.out.println(e);
					return null;
				}
				
			}
		};
	} 
}


		