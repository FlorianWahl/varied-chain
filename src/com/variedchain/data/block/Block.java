package com.variedchain.data.block;

import java.util.Date;

public class Block {
	public long blockId;
	public int blockVersion;
	public Hash hashPriv;
	public String payload;
	public String payloadType;
	public Hash hashPayload;
	public Date timePoint;
	public String txNum;
}
