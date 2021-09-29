package application.net.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log { //Logs main operations including user events. Helps when a user has a complaint, the log will have all needed information
	
	private static Log instance = null;
	private FileWriter myWriter;

	public static Log getInstance() throws IOException{
		if(instance == null)
			instance  = new Log();
		
		return instance ;
	}
	
	public Log() throws IOException {
		File log = new File("ServerLog.txt");
	    if (log.createNewFile()) {
	    	System.out.println("[SERVER] File created: " + log.getName());
	    } else {
	    	System.out.println("[SERVER] File log already exists.");
	    }
	    myWriter = new FileWriter("ServerLog.txt", true);
	}
	
	public synchronized void addEvent(String event) {
		try {
		      myWriter.append(event + System.lineSeparator());
		      myWriter.flush();
		} catch (IOException e) {
		      e.printStackTrace();
		}
	}

}
