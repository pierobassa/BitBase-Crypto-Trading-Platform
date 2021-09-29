package application.net.common;
import java.io.Serializable;

public class User implements Serializable { //Handles a user
	private static final long serialVersionUID = 5257249663796286997L;
	
	private String username;
	private String name;
	private String surname;
	private String password;
	
	
	public User(String username, String password, String name, String surname) {
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}

}
