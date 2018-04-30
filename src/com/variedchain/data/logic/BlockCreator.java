package com.variedchain.data.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.variedchain.data.block.Block;

public class BlockCreator extends BlockFactory {

	@Override
	public boolean addNewBlock(Block block) throws IOException {
		
		block.blockId = getSize();
		
		HasherBasic h = new SimpleHasher();
		block.hashPayload = h.calculateHash(block.payload.getBytes());
		
		if(block.blockId > 0) {
			byte[] privhash = loadhash(block.blockId-1);
			if(privhash == null) {
				return false;
			}
			block.hashPriv = privhash;
		}
		
		Gson gson=new Gson();
		FileWriter fw=new FileWriter(block.blockId+".block.json");
		gson.toJson(block,fw);
		fw.close();
		
		String b = gson.toJson(block);
		byte[] blockHash = h.calculateHash(b.getBytes());
		
		FileWriter fw2 = new FileWriter(block.blockId+".hash.json");
		gson.toJson(blockHash,fw2);
		fw2.close();
		return true;
	}

	@Override
	public boolean checkBlock(long blockID) {
		try {
			Gson gson=new Gson();
			FileReader fr = new FileReader(blockID+".hash.json");
			byte[] fsh = gson.fromJson(fr, byte[].class);
			fr.close();
			
			Path path = Paths.get(blockID+".block.json");
			byte[] data = Files.readAllBytes(path);
			
			HasherBasic h = new SimpleHasher();
			byte[] ch = h.calculateHash(data);
			
			if(ch.length != fsh.length) {
				return false;
			}
			
			for(int i = 0; i < ch.length; i++) {
				if(ch[i] != fsh[i]) {
					return false;
				}
			}
			
			
			
		} catch (IOException e) {
			
			return false;
		}
		
		return true;
	}

	@Override
	public byte[] loadhash(long blockId) {
		
		if(!checkBlock(blockId)) {
			return null;
		}
		
		Gson gson=new Gson();
		byte[] fsh = null;

		try {
			FileReader fr = new FileReader(blockId+".hash.json");
			fsh = gson.fromJson(fr, byte[].class);
			fr.close();
		} catch (IOException e) {
			
			return null;
		}
		
		return fsh;
	}

	@Override
	public long getSize() {
		int size = 0;
	    while(checkBlock(size)) {
	    	size++;
	    	
	    }
		return size;
	}

	@Override
	public Block loadBlock(long blockID) {
		
		if(!checkBlock(blockID)) {
			return null;
		}
		
		Gson gson=new Gson();

		try {
			FileReader fr = new FileReader(blockID+".block.json");
			Block b = gson.fromJson(fr, Block.class);
			fr.close();
			return b;
		} catch (IOException e) {
			return null;
		}
		
	}

}
