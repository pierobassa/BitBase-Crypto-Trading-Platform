package application.net.server;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class UsersHandler { //Handles the users that are logged in

	private static HashMap<String,MessagesHandler> users = new HashMap<String, MessagesHandler>();
	private static Map <String, ObjectOutputStream> userStreams = new HashMap<String, ObjectOutputStream>(); //Needed when a user want to send an asset and the recipient receives a notification if he is online
	
	public synchronized static boolean insertUser(String username, MessagesHandler handler) {
		if(users.containsKey(username))
			return false;
		users.put(username, handler);
		return true;
	}
	
	public synchronized static void addUserStream(String username, ObjectOutputStream objOut) {
		if(userStreams.containsKey(username))
			return;
		userStreams.put(username, objOut);
	}
	
	public synchronized static ObjectOutputStream getStream(String username) {
		return userStreams.get(username);
	}
	
	public synchronized static void removeUser(String username) {
		users.remove(username);
		userStreams.remove(username);
	}
	
	public synchronized static Vector<String> allUsers() {
		Vector<String> onlineUsers = new Vector<String>();
		for(var s : users.keySet())
			onlineUsers.add(s);
		return onlineUsers;
	}
}
