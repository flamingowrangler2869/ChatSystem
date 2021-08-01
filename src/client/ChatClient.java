package client;

import java.io.IOException;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class ChatClient {
	private String hostname;
	private int port;
	private String username;
	
	public ChatClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	public void execute() {
		try {
			Socket socket = new Socket(hostname, port);
			
			System.out.println("connected to the chat server");
			
			new ReadThread(socket, this).start();
			new WriteThread(socket, this).start();
		} catch(UnknownHostException e) {
			System.out.println("server not found: " + e.getMessage());
		} catch(IOException e) {
			System.out.println("i/o error: " + e.getMessage());
		}
	}

	

	void setUsername(String username) {
		this.username = username;
	}
	
	String getUsername() {
		return this.username;
	}
	
	public static void main(String[] args) {
		if (args.length < 2) return;
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		
		ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}
}
