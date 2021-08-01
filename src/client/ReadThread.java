package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
	private BufferedReader reader;
	private Socket socket;
	private ChatClient client;
	
	public ReadThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
		
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch(IOException e) {
			System.out.println("error getting input stream: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				String response = reader.readLine();
				System.out.println("\n" + response);
				
				// prints the username after displaying the server's message
				if (client.getUsername() != null) {
					System.out.print(client.getUsername() + "> ");
				}
			} catch(IOException e) {
				System.out.println("error reading from server: " + e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}
	
}
