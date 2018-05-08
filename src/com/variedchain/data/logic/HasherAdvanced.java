package com.variedchain.data.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HasherAdvanced extends HasherBasic {

	@Override
	public byte[] calculateHash(byte[] data) {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return digest.digest(data);
	}

}
