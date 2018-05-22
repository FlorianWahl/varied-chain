package com.variedchain.net.logic;

import java.io.*;
import java.net.*;

public class Server {

	public static void main(String argv[]) throws Exception {
		
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			if(clientSentence.equals("Join")){
				System.out.println("Client wants to Join");
				System.out.println(welcomeSocket.getInetAddress());
			}
			outToClient.writeBytes("OK");
		}
	}
}
