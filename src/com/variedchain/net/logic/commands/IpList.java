package com.variedchain.net.logic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;

import com.variedchain.data.logic.Database;

public class IpList extends Basic {

	@Override
	public boolean yourCommand(String input) {

		return input.equals("iplist");
	}

	@Override
	public void doit(BufferedReader in, DataOutputStream out, String remote) throws IOException {
		if (!Database.isconnected(remote)) {
			out.writeUTF("502: Not Connected"); // Not connected
			return;
		}
		out.writeUTF("OK\n");
		for (String ip : Database.getlist()) {
			if(!Database.getlist().contains(ip)) {
				out.writeUTF(ip);
				out.writeUTF("\n");
			}
		}
		out.writeUTF("END");

	}

}
