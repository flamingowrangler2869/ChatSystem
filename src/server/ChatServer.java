package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.mocha.impts.ANSIColors;

public class ChatServer {
	private int port;
	private Set<String> usernames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();
	
	public ChatServer(int port) {
		this.port = port;
	}

	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("chat server is listening to port " + port);
			
			while (true) {
				Socket socket = serverSocket.accept();
				
				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();

				System.out.println(newUser.getName() + " has been assigned to perform tasks on this chatroom");
			}
		} catch(IOException e) {
			System.out.println(ANSIColors.RED + "Error in main.ChatServer: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Syntax: java ChatServer <port>");
			System.exit(0);
		}
		
		int port = Integer.parseInt(args[0]);
		
		ChatServer server = new ChatServer(port);
		server.execute();
	}
	
	void broadcast(String message, UserThread excludeUser) {
		for (UserThread recipient : userThreads) {
			if (recipient != excludeUser) {
				recipient.sendMessage(message);
			}
		}
	}
	
	void addUserName(String username) {
		usernames.add(username);
	}
	
	void removeUser(String username, UserThread user) {
		boolean removed = usernames.remove(username);
		if (removed) {
			userThreads.remove(user);
			System.out.println(username + " was ejected");
			System.out.println(username + " was not the imposter");
		}
	}
	
	Set<String> getUserNames() {
		return this.usernames;
	}
	
 	
	
	boolean empty() {
		return this.usernames.isEmpty();
	}

}
