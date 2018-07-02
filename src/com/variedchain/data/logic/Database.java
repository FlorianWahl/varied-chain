package com.variedchain.data.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
	
	private static ArrayList<String> nodeiplist = new ArrayList<String>();
	private static BlockFactory bf = new BlockCreator();
	
	public static boolean isconnected(String ip) {
		if(nodeiplist.contains(ip)) {
			return true;
		}
		
		return false;
	}
	
	public static void addtolist(String ip) throws IOException {
		System.out.println("addtolist: " + ip);
		if(!nodeiplist.contains(ip)) {
			nodeiplist.add(ip);
		}
		
		FileWriter fw = new FileWriter("iplist");
		for (String nodeip : nodeiplist) {
				fw.write(nodeip+ "\n");
		}
		fw.close();
		
		
	}
	public static ArrayList<String> getlist(){
		return nodeiplist;
	}

	public static void addtolist(ArrayList<String> ipadresses) {
		if(ipadresses == null) {
			return;
		}
		
		for(String ip : ipadresses) {
			if(ip.trim().equals("OK") || ip.trim().equals("END") || ip.trim().equals("")) {
				continue;
			}
			try {
				addtolist(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static long getSize() {
		return bf.getSize();
	}

}
