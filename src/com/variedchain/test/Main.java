package com.variedchain.test;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.JsonIOException;
import com.variedchain.data.block.Block;
import com.variedchain.data.logic.BlockCreator;
import com.variedchain.data.logic.BlockFactory;

public class Main {

	public static void main(String[] args) throws JsonIOException, IOException {
		Block testBlock = new Block();
		testBlock.blockVersion = 1;
		testBlock.hashPayload = null;
		testBlock.hashPriv = null;
		testBlock.payload = "variedchani";
		testBlock.payloadType = "text/plain";
		testBlock.timePoint = new Date();
		testBlock.txNum = "1";

		BlockFactory bf = new BlockCreator();

		System.out.println(bf.addNewBlock(testBlock));
		System.out.println(bf.getSize());

		Scanner scan = new Scanner(System.in);

		while (true) {
			System.out.println("Bitte Blocknummer eingeben:");
			int id = scan.nextInt();

			Block b2 = bf.loadBlock(id);

			if (b2 == null) {
				System.out.println("Ungültige Blocknummer:" + id);
				return;
			} else {
				System.out.println(b2.payload);
			}
		}
	}

}
