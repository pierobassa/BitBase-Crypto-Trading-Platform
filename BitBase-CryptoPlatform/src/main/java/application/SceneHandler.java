package application;

import java.io.IOException;
import application.controller.CryptoScreenController;
import application.controller.HomeController;
import application.controller.LoadingScreenContoller;
import application.data.tasks.Update30DayPrices;
import application.data.tasks.UpdateCurrentPrices;
import application.data.tasks.UpdateNews;
import application.model.ClientLogic;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SceneHandler {
    private Scene scene;
    private Stage stage;
    
    private Parent loginRoot;
    private Parent homeRoot;
    private Parent rootLoading;
    private Parent signupRoot;
    private Parent portfolioRoot;
    private Parent allAssetsRoot;

	private static SceneHandler instance = null;
	
	private SceneHandler() {		
	}	
	
	public void init(Stage primaryStage) throws Exception {
		stage = primaryStage;
		
		setStageIcon();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Login.fxml"));
		loader = new FXMLLoader(getClass().getResource("/application/view/Login.fxml"));
    	loginRoot = (Parent) loader.load();
    	
    	loader = new FXMLLoader(getClass().getResource("/application/view/Signup.fxml"));
    	signupRoot = (Parent) loader.load();
    	
    	loader = new FXMLLoader(getClass().getResource("/application/view/Portfolio.fxml"));
    	portfolioRoot = (Parent) loader.load();
    	
    	ClientView.getInstance().setPortfolioController(loader.getController());
    	
    	loader = new FXMLLoader(getClass().getResource("/application/view/AllAssets.fxml"));
    	allAssetsRoot = (Parent) loader.load();
    	
    	ClientView.getInstance().setAllAssetsController(loader.getController());
    	
		scene = new Scene(loginRoot);
		stage.setScene(scene);
		stage.setTitle("BitBase");
		stage.setResizable(false);
		

		stage.show();
	}

	public static SceneHandler getInstance() {
		if(instance == null)
			instance = new SceneHandler();
		return instance;
	}        
    
	public void setLoadingScene() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Home.fxml"));
    	homeRoot = (Parent) loader.load();
    	HomeController homeController = loader.getController();
    	ClientView.getInstance().setHomeController(homeController);
    
    	runServices();
    	sendRequests();
		
    	loader = new FXMLLoader(getClass().getResource("/application/view/LoadingScreen.fxml"));
    	rootLoading = (Parent) loader.load();
    	
    	LoadingScreenContoller loadingScreenContoller = loader.getController();
    	ClientView.getInstance().setLoadingController(loadingScreenContoller);
    	
    	scene.setRoot(rootLoading);
    	stage.hide();
   
    	stage.setResizable(false);
    	stage.show();    	
    }
    
    private void sendRequests() throws IOException {
    	ClientLogic.getInstance().balance();
    	ClientLogic.getInstance().amountOwned();
    	ClientLogic.getInstance().transactions();
    	ClientLogic.getInstance().requestCryptoStats();
	}

	private void runServices() {
    	UpdateCurrentPrices updateCurrentPrices = new UpdateCurrentPrices();
    	updateCurrentPrices.run();
    	
    	Update30DayPrices update30DayPrices = new Update30DayPrices();
    	update30DayPrices.run();
    	
    	UpdateNews updateNews = new UpdateNews();
    	updateNews.run();
	}

	public void setHomeScene() throws Exception {
    	scene.setRoot(homeRoot);
    	stage.hide();
    	stage.setResizable(true);
    	setMinSizes();
    	stage.setMaxHeight(Double.MAX_VALUE);
    	stage.setMaxWidth(Double.MAX_VALUE);
    	stage.show();
    	
    }
    
 public void goToHome() throws Exception {	
    	scene.setRoot(homeRoot);
    	stage.setScene(scene);
    	stage.setResizable(true);
    	setMinSizes();
    }
    

 	public void setLoginScene() { 	
		scene.setRoot(loginRoot);
		stage.setScene(scene);
		stage.setResizable(false);
    	setMaxSizes();
	}
 	
 	public void setPortfolioScene() { 
		scene.setRoot(portfolioRoot);
		stage.setScene(scene);
		stage.setResizable(true);
		setMinSizes();
	}
 	
 	public void setAllAssetsScene() throws Exception {
    	scene.setRoot(allAssetsRoot);
    	stage.setScene(scene);
    	stage.setResizable(true);
    	setMinSizes();
    }
    
    
    public void setSignupScene() throws Exception {  
    	scene.setRoot(signupRoot);
    	stage.setResizable(false);
    }
    
	public void setCryptoScene(String crypto) throws IOException {
		FXMLLoader cryptoLoader = new FXMLLoader(getClass().getResource("/application/view/CryptoScreen.fxml"));
    	CryptoScreenController controller = new CryptoScreenController(crypto);
    	cryptoLoader.setController(controller);
    	
    	ClientView.getInstance().setCryptoScreenControlelr(controller);
    	
    	Parent cryptoRoot = (Parent) cryptoLoader.load();    
    
    	scene.setRoot(cryptoRoot);
    	stage.setResizable(true);
 
    	setMinSizes();
	}

	private void setMinSizes() {
		stage.setMinHeight(787);
    	stage.setMinWidth(1348);
	}   
	
	private void setMaxSizes() {
		stage.setMaxHeight(747);
    	stage.setMaxWidth(1328);
	}
	
	private void setStageIcon() {
		Image image = new Image(getClass().getResourceAsStream("/images/icons/stage-icon.png"));
		stage.getIcons().add(image);
	}
	
	public void setSceneBlur() {
		scene.setFill(Color.BLACK);
		GaussianBlur blur = new GaussianBlur();
		
		scene.getRoot().setEffect(blur);
	}
	
	public void removeSceneBlur() {
		scene.setFill(null);
		scene.getRoot().setEffect(null);
	}
}