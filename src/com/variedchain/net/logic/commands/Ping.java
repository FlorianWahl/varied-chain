package com.variedchain.net.logic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ping extends Basic {

	@Override
	public boolean yourCommand(String input) {
		// TODO Auto-generated method stub
		return input.equals("ping");
	}

	@Override
	public void doit(BufferedReader in, DataOutputStream out, String remote) throws IOException {
	   out.writeUTF("OK");

	}

}
