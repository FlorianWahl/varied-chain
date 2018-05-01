package com.variedchain.data.logic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.variedchain.data.block.Block;

public class BlockCreator extends BlockFactory {

	private HasherBasic hasher;
	private Gson gson;

	public BlockCreator() {
		hasher = new SimpleHasher();
		gson = new Gson();
	}

	@Override
	public boolean addNewBlock(Block block) throws IOException {
		block.blockId = getSize();
		block.hashPayload = hasher.calculateHash(block.payload.getBytes());
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

		String blockString = gson.toJson(block);
		byte[] blockHash = hasher.calculateHash(blockString.getBytes());

		FileWriter hashFileWriter = new FileWriter(block.blockId + ".hash.json");
		gson.toJson(blockHash, hashFileWriter);
		hashFileWriter.close();
		return true;
	}

	@Override
	public boolean checkBlock(long blockId) {
		try {
			FileReader hashFileReader = new FileReader(blockId + ".hash.json");
			byte[] fsh = gson.fromJson(hashFileReader, byte[].class);
			hashFileReader.close();

			Path path = Paths.get(blockId + ".block.json");
			byte[] data = Files.readAllBytes(path);
			byte[] ch = hasher.calculateHash(data);
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
	public byte[] loadhash(long blockId) {
		byte[] hash = null;
		if (!checkBlock(blockId)) {
			return null;
		}
		try {
			FileReader hashFileReader = new FileReader(blockId + ".hash.json");
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
		while (checkBlock(size)) {
			size++;
		}
		return size;
	}

	@Override
	public Block loadBlock(long blockId) {
		if (!checkBlock(blockId)) {
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
