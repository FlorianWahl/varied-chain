package com.variedchain.net.serverlogic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.variedchain.data.logic.BlockCreator;
import com.variedchain.data.logic.Database;

public class BackgroundAsync implements Runnable {

	static HashMap<String, TBC> tbcMap;

	@Override
	public void run() {
		for (;;) {
			for (String ip : Database.getlist()) {
				// TODO if ipadresse unavailable -> delete from iplist
				ArrayList<String> result = getdatafromserver(ip, "req");
				Database.addtolist(getdatafromserver(ip, "iplist"));
				result = getdatafromserver(ip, "blocks");
				checkBlocks(ip, result);
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
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
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

	public static void checkBlocks(String ip, ArrayList<String> blocks) {
		if (blocks == null || !blocks.get(0).trim().equals("OK")) {
			return;
		}

		if (Long.parseLong(blocks.get(1)) > Database.getSize()) {
			System.out.println("Anderer hat mehr blocks" + Long.parseLong(blocks.get(1)));
			Long newBlock = Database.getSize();
			downloadBlock(ip, newBlock);

		}

	}

	public static void downloadBlock(String ip, long blockID) {
		ArrayList<String> getblock = getdatafromserver(ip, "getblock " + blockID);

		if (getblock == null || !getblock.get(0).trim().equals("OK")) {
			return;
		}
		Database.recieveBlock(getblock.get(1));
		if (!Database.getHash(blockID).equals(getblock.get(2))) {
			System.out.println("Block doesnt equal " + blockID);
			// TODO abfrage mehrheit implementieren
			
			tbcMap = new HashMap<String, TBC>();
			for (String checkip : Database.getlist()) {
				ArrayList<String> checkgetblock = getdatafromserver(checkip, "getblock " + blockID);
				if (checkgetblock == null || !checkgetblock.get(0).trim().equals("OK")) {
					return;
				}
				if (tbcMap.containsKey(checkgetblock.get(2))) {
					tbcMap.get(checkgetblock.get(2)).anzahlHashMenge++;
				} else {
					tbcMap.put(checkgetblock.get(2), new TBC(checkip, checkgetblock.get(2)));
				}
			}
			TBC ba = biggestAmountHash();
			if(ba.hashwert.equals(Database.getHash(blockID)))return;
			try {
				FileUtils.cleanDirectory(new File(BlockCreator.blockpath));
				for(long i = 0; i<=blockID; i++) {
					downloadBlock(ip, i);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}

	}

	public static TBC biggestAmountHash() {

		TBC hilfe = null;

		for (String hash : tbcMap.keySet()) {
			TBC element = tbcMap.get(hash);
			if (hilfe == null || element.anzahlHashMenge > hilfe.anzahlHashMenge) {
				hilfe = element;
			}
		}

		return hilfe;

	}

}
