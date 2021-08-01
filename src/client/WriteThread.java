package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    
	public WriteThread(Socket socket, ChatClient client) {
		this.socket = socket;
		this.client = client;
		
		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
		} catch (IOException e) {
			System.out.println("error getting output stream: " + e.getMessage());
			e.printStackTrace();
		}
	}
    
	public void run() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\nenter ur name: ");
		String username = sc.nextLine();
		client.setUsername(username);
		writer.println(username);
		
		String text;
		
		do {
			System.out.print(username + "> ");
			text = sc.nextLine();
			writer.print(text);
		} while (!text.equals("!eject"));
		
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("error writing to server: " + e.getMessage());
		}
		
		sc.close();
	}
    
}
	