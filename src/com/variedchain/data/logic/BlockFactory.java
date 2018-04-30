package com.variedchain.data.logic;

import java.io.IOException;

import com.variedchain.data.block.Block;

public abstract class BlockFactory {
	public abstract boolean addNewBlock(Block b) throws IOException;
	public abstract boolean checkBlock(long blockID);
	public abstract byte[] loadhash(long blockId);
	public abstract long getSize();
	public abstract Block loadBlock(long blockID);
}
