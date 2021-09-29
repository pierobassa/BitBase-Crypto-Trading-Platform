package application.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import org.json.JSONObject;
import application.model.News;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DataServiceArticles extends Service<Vector<News>>{
	
	private  String dati;
	
	public void setDati(String dati) {
		this.dati = dati;
	}

	@Override
	protected Task<Vector<News>> createTask() {
		return new Task<Vector<News>>() {

			@Override
			protected Vector<News> call() throws Exception {
				try {
					Vector<News> news = new Vector<News>();
					
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
					String title1 = jsonResponse.getJSONArray("articles").getJSONObject(0).get("title").toString().replaceAll("[^\\w\\s\\$]", "");;
					String about1 = jsonResponse.getJSONArray("articles").getJSONObject(0).get("description").toString().replaceAll("[^\\w\\s\\$]", "");//Replace non supported chars
					String title2 = jsonResponse.getJSONArray("articles").getJSONObject(1).get("title").toString().replaceAll("[^\\w\\s\\$]", "");;
					String about2 = jsonResponse.getJSONArray("articles").getJSONObject(1).get("description").toString().replaceAll("[^\\w\\s\\$]", "");
					String title3 = jsonResponse.getJSONArray("articles").getJSONObject(2).get("title").toString().replaceAll("[^\\w\\s\\$]", "");;
					String about3 = jsonResponse.getJSONArray("articles").getJSONObject(2).get("description").toString().replaceAll("[^\\w\\s\\$]", "");
					news.add(new News(title1, about1));
					news.add(new News(title2, about2));
					news.add(new News(title3, about3));
					
					return news;
					
				}
				catch(Exception e) {
					System.out.println(e);
					return null;
				}
			}
		};
	}

}
