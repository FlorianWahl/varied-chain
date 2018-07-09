package com.variedchain.net.serverlogic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import com.variedchain.data.logic.Database;

public class GetBlock extends Basic {
	Long blockID;
	@Override
	public boolean yourCommand(String input) {
		if(input.startsWith("getblock ")) {
			blockID = Long.parseLong(input.substring(9));
			return true;
		}
		return false;
	}

	@Override
	public void doit(BufferedReader in, DataOutputStream out, String remote) throws IOException {
		if (!Database.isconnected(remote)) {
			out.writeUTF("502: Not Connected"); // Not connected
			return;
		}
		out.writeUTF("OK\n");
		out.writeUTF("" + Database.getBlock(blockID));
		out.writeUTF("" + Database.getHash(blockID));
	}

}
