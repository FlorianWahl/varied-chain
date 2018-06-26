package com.variedchain.data.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
	
	private static ArrayList<String> nodeiplist = new ArrayList<String>();
	
	public static boolean isconnected(String ip) {
		for (String nodeip : nodeiplist) {
			if(ip.equals(nodeip)) {
				return true;
			}
		}
		return false;
	}
	
	public static void addtolist(String ip) throws IOException {
		nodeiplist.add(ip);
		FileWriter fw = new FileWriter("iplist");
		for (String nodeip : nodeiplist) {
			fw.write(ip);
		}
		fw.close();
		
		
	}
	public static ArrayList<String> getlist(){
		return nodeiplist;
	}

}
