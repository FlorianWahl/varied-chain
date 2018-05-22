package com.variedchain.data.logic;

import com.variedchain.data.block.Hash;

public abstract class HasherBasic {
	abstract public Hash calculateHash(byte[] data);
}
