package com.variedchain.data.logic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.variedchain.data.block.Block;
import com.variedchain.data.block.Hash;

public class BlockCreator extends BlockFactory {

	private HasherBasic hasher;
	private HasherAdvanced ahasher;
	private Gson gson;
	
	public BlockCreator() {
		hasher = new SimpleHasher();
		ahasher = new HasherAdvanced();
		gson = new Gson();
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
			Hash privhash = loadhash(block.blockId - 1);
			if (privhash == null) {
				return false;
			}
			block.hashPriv = privhash;
		}
		FileWriter blockFileWriter = new FileWriter(block.blockId + ".block.json");
		gson.toJson(block, blockFileWriter);
		blockFileWriter.close();
		Hash blockHash;
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
			Hash fsh = gson.fromJson(hashFileReader, Hash.class);
			hashFileReader.close();

			Path path = Paths.get(id + ".block.json");
			byte[] data = Files.readAllBytes(path);
			HasherBasic hasher = (HasherBasic)Class.forName(fsh.method).newInstance();
			if(hashValidate(hasher.calculateHash(data), fsh)){
				if(id==0){
					return true;
				}
				
				FileReader hashFileReader2 = new FileReader((id-1) + ".hash.json");
				Hash fsh2 = gson.fromJson(hashFileReader2, Hash.class);
				hashFileReader2.close();
				
				Block b = loadBlock(id, false);
				return hashValidate(b.hashPriv, fsh2);
			}
			
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			return false;
		}
		return false;
	}
	
	public boolean hashValidate(Hash ch, Hash fsh) {
		if (ch.value.length != fsh.value.length) {
			return false;
		}
		for (int i = 0; i < ch.value.length; i++) {
			if (ch.value[i] != fsh.value[i]) {
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
			Hash ch;
			if (block.blockVersion == 1) {
				 ch = hasher.calculateHash(data);
			} else {
				 ch = ahasher.calculateHash(data);
			}

			if (ch.value.length != fsh.length) {
				return false;
			}
			for (int i = 0; i < ch.value.length; i++) {
				if (ch.value[i] != fsh[i]) {
					return false;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public Hash loadhash(long id) {
		Hash hash = null;
		if (!checkBlockById(id)) {
			return null;
		}
		try {
			FileReader hashFileReader = new FileReader(id + ".hash.json");
			hash = gson.fromJson(hashFileReader, Hash.class);
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
		return loadBlock(blockId, true);
	}
	
	private Block loadBlock(long blockId, boolean check) {
		if (check && !checkBlockById(blockId)) {
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

	@Override
	public String convertBlock(long blockId) {
		List<String> ret;
		try {
			ret = Files.readAllLines(Paths.get(blockId + ".block.json"));
		} catch (IOException e) {
			return null;
		}
		return ret.toString();
	}

	@Override
	public Block stringToBlock(String block) {
		Block[] blockarray = gson.fromJson(block, Block[].class);
		Block ret = blockarray[0];
		return ret;
	}

	@Override
	public String getHash(Long blockID) {
		List<String> ret;
		try {
			ret = Files.readAllLines(Paths.get(blockID + ".hash.json"));
		} catch (IOException e) {
			return null;
		}
		return ret.toString();
	}
	
}
