package com.variedchain.net.logic.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;

public abstract class Basic {
	
	public abstract boolean yourCommand(String input);
	public abstract void doit(BufferedReader in, DataOutputStream out, String remote ) throws IOException;

}
