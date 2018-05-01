package com.variedchain.data.block;

import java.util.Date;

public class Block {
	public long blockId;
	public int blockVersion;
	public byte[] hashPriv;
	public String payload;
	public String payloadType;
	public byte[] hashPayload;
	public Date timePoint;
	public String txNum;
}
