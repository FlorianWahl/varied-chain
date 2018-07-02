package com.variedchain.net.logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.variedchain.data.logic.Database;

public class BackgroundAsync implements Runnable {

	@Override
	public void run() {
		for (;;) {
			System.out.println("Hello Background");
			for (String ip : Database.getlist()) {
				//TODO if ipadresse unavailable -> delete from iplist
				ArrayList<String> result = getdatafromserver(ip, "req");
				Database.addtolist(getdatafromserver(ip, "iplist"));
				result = getdatafromserver(ip, "blocks");
				checkBlocks(result);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<String> getdatafromserver(String server, String command) {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			Socket socket = new Socket(server, 6789);
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataInputStream br = new DataInputStream(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(command + "\n");
			String s;
			out.flush();
			while ((s = br.readUTF()) != null) {
				ret.add(s);
			}
			br.close();
			out.close();
			return ret;

		} catch (EOFException e) {
			return ret;
		} catch (Exception e) {
			return null;
		}

	}
	
	public static void checkBlocks(ArrayList<String> blocks) {
		if(blocks == null || blocks.get(0).equals("OK")) {
			return;
		}
		
		if(Long.parseLong(blocks.get(1)) > Database.getSize()) {
			System.out.println("Anderer hat mehr blocks");
		}
		
	}
}
