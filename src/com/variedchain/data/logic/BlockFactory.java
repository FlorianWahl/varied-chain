package com.variedchain.data.logic;

import java.io.IOException;

import com.variedchain.data.block.Block;

public abstract class BlockFactory {
	public abstract boolean addNewBlock(Block block) throws IOException;

	public abstract boolean checkBlockById(long id);
	
	public abstract boolean checkBlock(Block block);

	public abstract byte[] loadhash(long blockId);

	public abstract long getSize();

	public abstract Block loadBlock(long blockId);
}
