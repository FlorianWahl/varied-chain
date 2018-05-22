package com.variedchain.data.block;

public class Hash {
	public Hash(String canonicalName, byte[] digest) {
		this.method = canonicalName;
		this.value = digest;
	}
	public String method;
	public byte[] value;
}
