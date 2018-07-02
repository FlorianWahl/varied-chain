package com.variedchain.net.serverlogic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import com.variedchain.data.logic.Database;

public class Blocks extends Basic {

	@Override
	public boolean yourCommand(String input) {
		
		return input.equals("blocks");
	}

	@Override
	public void doit(BufferedReader in, DataOutputStream out, String remote) throws IOException {
		if (!Database.isconnected(remote)) {
			out.writeUTF("502: Not Connected"); // Not connected
			return;
		}
		out.writeUTF("OK\n");
		out.writeUTF("" + Database.getSize());	
	}

}
