package com.variedchain.net.logic;

import java.io.BufferedReader;
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
			getdatafromserver(ip, "ping");
			}
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public static ArrayList<String> getdatafromserver(String server, String command) {
		try {
			Socket socket = new Socket(server, 6789);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(command);
			ArrayList<String> ret = new ArrayList<String>();
			String s;
			while ((s = br.readLine()) != null) {
				ret.add(s);
			}
			br.close();
			out.close();
			return ret;

		} catch (Exception e) {
			return null;
		}

	}
}
