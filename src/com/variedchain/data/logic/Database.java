package com.variedchain.data.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
	
	private static ArrayList<String> nodeiplist = new ArrayList<String>();
	
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

}
