package com.variedchain.net.serverlogic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;

import com.variedchain.data.logic.Database;

public class Request extends Basic {
	
	String[] badiplist = new String[] { "10.41.127.11", "23.52.234.2"};

	@Override
	public boolean yourCommand(String input) {
		
		return input.equals("req");
	}

	@Override
	public void doit(BufferedReader in, DataOutputStream out, String remote) throws IOException {
		
		
		for (String badip : badiplist) {
			if(remote.equals(badip)) {
				out.writeUTF("501: Permission Denied"); // Blacklist
				System.out.println("Permission Denied for: " + remote);
				return;
			}
		}
		
		//Sollte IP OK sein
		//TODO: Connectionspeed
		out.writeUTF("OK");
		System.out.println("Node: " + remote + " connected");
		Database.addtolist(remote);
		
	}
	
	

}
