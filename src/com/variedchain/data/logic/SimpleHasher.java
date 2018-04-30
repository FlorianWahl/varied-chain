package com.variedchain.data.logic;

import java.nio.ByteBuffer;

public class SimpleHasher extends HasherBasic {

	@Override
	public byte[] calculateHash(byte[] data) {
		long sum = 0;
		for(int i = 0; i < data.length; i++) {
			sum = sum + data[i];
			if(i%2==1) {
				sum = sum + 2 * data[i];
			}
		}

		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(sum);
		return buffer.array();
	}
}
