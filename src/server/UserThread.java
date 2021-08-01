package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
	private Socket socket;
	private ChatServer server;
	private PrintWriter writer;
	
	public UserThread(Socket socket, ChatServer server) {
		this.socket = socket;
		this.server = server;
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
			
			printUsers();
			
			String username = reader.readLine();
			server.addUserName(username);
			
			String serverMessage = "new crewmate: " + username;
			server.broadcast(serverMessage, this);
			
			String clientMessage;
			
			do {
				clientMessage = reader.readLine();
				serverMessage = String.format("%s> %s", username, clientMessage);
				server.broadcast(serverMessage, this);
			} while (!clientMessage.equals("!eject"));
			
			server.removeUser(username, this);
			socket.close();
			
			serverMessage = username + " was ejected";
			server.broadcast(serverMessage, this);
		} catch(IOException e) {
			System.out.println("Error in main.UserThread: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	void printUsers() {
		if (!server.empty()) {
			writer.println("crewmates onboard: " + server.getUserNames());
		} else {
			writer.println("no one is onboard rn. you are going to die");
		}
	}
	
	void sendMessage(String message) {
		writer.println(message);
	}
	
}
