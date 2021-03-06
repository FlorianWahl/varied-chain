package com.variedchain.net.serverlogic;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.variedchain.data.logic.Database;
import com.variedchain.net.serverlogic.commands.Basic;
import com.variedchain.net.serverlogic.commands.Blocks;
import com.variedchain.net.serverlogic.commands.GetBlock;
import com.variedchain.net.serverlogic.commands.IpList;
import com.variedchain.net.serverlogic.commands.Ping;
import com.variedchain.net.serverlogic.commands.Request;

public class Server {

	public static void main(String argv[]) throws Exception {

		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		Basic[] commands = new Basic[] { new Request(), new IpList(), new Ping(), new Blocks(), new GetBlock() };

		File file = new File("iplist");
		if (file.exists()) {
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				String nextip = sc.nextLine();
				Database.addtolist(nextip);
				System.out.println(nextip);
			}

			sc.close();
		}

		Thread t1 = new Thread(new BackgroundAsync());
		t1.start();
		
		while (true) {
			if (!t1.isAlive()) t1.run();
			
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
			outToClient.close();
			inFromClient.close();
		}
	}
}
