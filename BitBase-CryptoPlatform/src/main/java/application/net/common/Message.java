package application.net.common;

import java.io.Serializable;

public class Message implements Serializable{ //Used for communications between client and server. Helps to send a header that specifies the message and information containing the Object
	private static final long serialVersionUID = 2580697741802203017L;
	private String header;
	private Object information;
	
	public Message(String header, Object information) {
		this.header = header;
		this.information = information;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Object getInformation() {
		return information;
	}

	public void setInformation(Object information) {
		this.information = information;
	}
}
