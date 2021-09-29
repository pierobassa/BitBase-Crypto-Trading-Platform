package application.net.common;

public class Protocol {//Handles Strings that are constant to facilitate readability in Client-Server communications
	
	public final static String LOGIN = "login";
	public final static String REGISTRATION = "registration";
	
	public final static String OK = "ok";
	public final static String ERROR = "Error during connection to server. Please try again later.";
	public final static String AUTHENTICATION_ERROR = "Invalid username/password"; 
	public final static String USER_LOGGED_ERROR = "User already logged in";
	public final static String USER_EXISTS_ERROR = "user exists";
	
	public final static String BUY = "buy";
	public final static String SELL = "sell";
	public final static String DEPOSIT = "deposit";
	public final static String WITHDRAW = "withdraw";
	public final static String SEND = "send";
	
	public final static String TRANSACTION = "transaction request";
	public final static String TRANSACTION_FAILED = "transaction failed";
	public final static String TRANSACTION_SUCCESSFUL = "transaction successful";
	
	public final static String BALANCE_FAILED = "balance request failed";
	public final static String BALANCE_REQUEST = "balance request";
	public final static String BALANCE_RESPONSE = "balance request";
	
	public final static String AMOUNT_OWNED_FAILED = "amount owned request failed";
	public final static String AMOUNT_OWNED_REQUEST = "amount owned request";
	public final static String AMOUNT_OWNED_RESPONSE = "amount owned response";
	
	public final static String DEPOSIT_REQUEST = "deposit request";
	public final static String DEPOSIT_FAILED = "deposit request failed";
	public final static String DEPOSIT_RESPONSE = "deposit response";
	
	public final static String TRANSACTIONS_REQUEST = "user transactions request";
	public final static String TRANSACTIONS_FAILED = "user transactions request failed";
	public final static String TRANSACTIONS_RESPONSE = "user transactions response";
	
	public final static String WITHDRAW_REQUEST = "withdraw request";
	public final static String WITHDRAW_FAILED = "withdraw request failed";
	public final static String WITHDRAW_RESPONSE = "withdraw response";
	
	public final static String SEND_REQUEST = "send request";
	public final static String SEND_FAILED = "send request failed";
	public final static String SEND_RESPONSE = "send response";
	public final static String USER_INEXISTENT = "user does not exist";
	public final static String RECEIVED_ASSET = "received asset from user";
	
	public final static String UPDATE_USERNAME_REQUEST = "update username request";
	public final static String UPDATE_USERNAME_FAILED = "update username request failed";
	public final static String UPDATE_USERNAME_RESPONSE = "update username response";
	
	public final static String UPDATE_USERNAME_PASSWORD_REQUEST = "update username and password request";
	public final static String UPDATE_USERNAME_PASSWORD_FAILED = "update username and password request failed";
	public final static String UPDATE_USERNAME_PASSWORD_RESPONSE = "update username and password response";
	public final static String PASSWORD_INVALID = "new password is not valid";
	public final static String PASSWORD_CHANGE_FAILED = "password change failed";
	public static final String UPDATE_PASSWORD_REQUEST = "update password request";
	public static final String UPDATE_PASSWORD_RESPONSE = "update password response";
	
	public static final String CRYPTO_STATS_REQUEST = "crypto stats request";
	public static final String CRYPTO_STATS_REQUEST_FAILED = "Server is updating. Retry later.";
	public static final String CRYPTO_STATS_RESPONSE = "crypto stats response";
	
	//Asset consts
	public final static String USD = "USD";
	public final static String BTC = "BTC";
	public final static String ETH = "ETH";
	public final static String BNB = "BNB";
	public final static String ADA = "ADA";
	public final static String DOT = "DOT";
	public final static String LTC = "LTC";
	public final static String XRP = "XRP";
	public final static String SOL = "SOL";
	public final static String TRX = "TRX";
}
 