package variedchain.data.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import variedchain.data.block.Block;

public class Main {

	public static void main(String[] args) throws JsonIOException, IOException {
		Block testBlock = new Block();
		testBlock.blockId=0;
		testBlock.blockVersion=1;
		testBlock.hashPayload=null;
		testBlock.hashPriv=null;
		testBlock.payload="Test";
		testBlock.payloadType="text/plain";
		testBlock.timePoint=new Date();
		testBlock.txNum="1";

		Gson gson=new Gson();
		FileWriter fw=new FileWriter("testfile.json");
		gson.toJson(testBlock,fw);
		fw.close();
		
		System.out.println("finished");

	}

}
