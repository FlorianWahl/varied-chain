package com.variedchain.data.logic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.variedchain.data.block.Block;

public class BlockCreator extends BlockFactory {

	private HasherBasic hasher;
	private HasherAdvanced ahasher;
	private Gson gson;
	private HasherBasic[] versions;
	
	public BlockCreator() {
		hasher = new SimpleHasher();
		ahasher = new HasherAdvanced();
		gson = new Gson();
		versions = new HasherBasic[2];
		versions[0] = hasher;
		versions[1] = ahasher;
	}

	@Override
	public boolean addNewBlock(Block block) throws IOException {
		block.blockId = getSize();
		if (block.blockVersion == 1) {
			block.hashPayload = hasher.calculateHash(block.payload.getBytes());
		} else {
			block.hashPayload = ahasher.calculateHash(block.payload.getBytes());
		}
		if (block.blockId > 0) {
			byte[] privhash = loadhash(block.blockId - 1);
			if (privhash == null) {
				return false;
			}
			block.hashPriv = privhash;
		}
		FileWriter blockFileWriter = new FileWriter(block.blockId + ".block.json");
		gson.toJson(block, blockFileWriter);
		blockFileWriter.close();
		byte[] blockHash;
		String blockString = gson.toJson(block);
		if (block.blockVersion == 1) {
			blockHash = hasher.calculateHash(blockString.getBytes());
		} else {
			blockHash = ahasher.calculateHash(blockString.getBytes());
		}

		FileWriter hashFileWriter = new FileWriter(block.blockId + ".hash.json");
		gson.toJson(blockHash, hashFileWriter);
		hashFileWriter.close();
		return true;
	}

	@Override
	public boolean checkBlockById(long id) {
		try {
			FileReader hashFileReader = new FileReader(id + ".hash.json");
			byte[] fsh = gson.fromJson(hashFileReader, byte[].class);
			hashFileReader.close();

			Path path = Paths.get(id + ".block.json");
			byte[] data = Files.readAllBytes(path);
			
			for(int i = 0; i<versions.length; i++) {
				if(hashValidate(versions[i].calculateHash(data), fsh)) {
					return true;
				}
			}
			
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	public boolean hashValidate(byte[] ch, byte[] fsh) {
		if (ch.length != fsh.length) {
			return false;
		}
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] != fsh[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean checkBlock(Block block) {
		try {
			FileReader hashFileReader = new FileReader(block.blockId + ".hash.json");
			byte[] fsh = gson.fromJson(hashFileReader, byte[].class);
			hashFileReader.close();

			Path path = Paths.get(block.blockId + ".block.json");
			byte[] data = Files.readAllBytes(path);
			byte[] ch;
			if (block.blockVersion == 1) {
				 ch = hasher.calculateHash(data);
			} else {
				 ch = ahasher.calculateHash(data);
			}

			if (ch.length != fsh.length) {
				return false;
			}
			for (int i = 0; i < ch.length; i++) {
				if (ch[i] != fsh[i]) {
					return false;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public byte[] loadhash(long id) {
		byte[] hash = null;
		if (!checkBlockById(id)) {
			return null;
		}
		try {
			FileReader hashFileReader = new FileReader(id + ".hash.json");
			hash = gson.fromJson(hashFileReader, byte[].class);
			hashFileReader.close();
		} catch (IOException e) {
			return null;
		}
		return hash;
	}

	@Override
	public long getSize() {
		int size = 0;
		while (checkBlockById(size)) {
			size++;
		}
		return size;
	}

	@Override
	public Block loadBlock(long blockId) {
		if (!checkBlockById(blockId)) {
			return null;
		}
		try {
			FileReader blockFileReader = new FileReader(blockId + ".block.json");
			Block block = gson.fromJson(blockFileReader, Block.class);
			blockFileReader.close();
			return block;
		} catch (IOException e) {
			return null;
		}
	}
}
