package com.variedchain.data.logic;

import java.nio.ByteBuffer;

import com.variedchain.data.block.Hash;

public class SimpleHasher extends HasherBasic {

	@Override
	public Hash calculateHash(byte[] data) {
		long sum = 0;
		for (int i = 0; i < data.length; i++) {
			sum = sum + data[i];
			if (i % 2 == 1) {
				sum = sum + 2 * data[i];
			}
		}
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(sum);
		return new Hash(SimpleHasher.class.getCanonicalName(), buffer.array());
	}
}
