package com.variedchain.net.logic;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.variedchain.data.logic.Database;
import com.variedchain.net.logic.commands.Basic;
import com.variedchain.net.logic.commands.IpList;
import com.variedchain.net.logic.commands.Ping;
import com.variedchain.net.logic.commands.Request;

public class Server {

	public static void main(String argv[]) throws Exception {

		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		Basic[] commands = new Basic[] { new Request(), new IpList(), new Ping() };
		ArrayList<String> nodeiptmp = new ArrayList<String>();

		File file = new File("iplist");
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			nodeiptmp.add(sc.nextLine());
		}
		sc.close();
		for (String ip : nodeiptmp) {
			Database.addtolist(ip);
		}
		
		Thread background = new Thread(new BackgroundAsync());
		background.start();
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			SocketAddress remoteadress = connectionSocket.getRemoteSocketAddress();

			String remoteadd = remoteadress.toString();
			int remoteaddressposition = remoteadd.indexOf(":");
			String remoteaddressfinal = remoteadd.substring(1, remoteaddressposition);
			System.out.println("Request from: " + remoteaddressfinal);

			for (Basic onecommand : commands) {
				if (onecommand.yourCommand(clientSentence)) {
					onecommand.doit(inFromClient, outToClient, remoteaddressfinal);
					break;
				}

			}

		}
	}
	
}
