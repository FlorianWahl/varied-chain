package com.variedchain.data.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.variedchain.data.block.Hash;

public class HasherAdvanced extends HasherBasic {

	@Override
	public Hash calculateHash(byte[] data) {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Hash(HasherAdvanced.class.getCanonicalName(), digest.digest(data));
	}

}
