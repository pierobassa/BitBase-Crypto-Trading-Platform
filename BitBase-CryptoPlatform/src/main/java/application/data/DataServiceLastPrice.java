package application.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DataServiceLastPrice extends Service<Double>{
	
	private String data;
	
	public void setData(String data) {
		this.data = data;
	}

	@Override
	protected Task<Double> createTask() {
		return new Task<Double>() {
			
			@Override
			protected Double call() throws Exception {
				try{
					String url = data;
			
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
					
					JSONObject result = jsonResponse.getJSONObject("result");
					return result.getDouble("price");
				}
				catch (Exception e) {	
					System.out.println(e);
					return null;
				}
			}
		};
	}

}
