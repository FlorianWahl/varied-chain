package com.variedchain.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.google.gson.Gson;
import com.variedchain.data.block.Block;
import com.variedchain.data.logic.BlockCreator;
import com.variedchain.data.logic.BlockFactory;
import com.variedchain.data.logic.Database;

public class ChainGui extends JFrame {

	static JFrame maingui;
	static JPanel parentPanel, childPanel1, childPanel2;
	static JButton btn;
	static JTextField yourContent;
	static JTable blockchaintabelle;
	static String data[][];
	static BlockCreator blockCreator = new BlockCreator();
	static JScrollPane scrollPane = new JScrollPane();
	static JScrollPane jScrollPane;
	private static Gson gson = new Gson();
	public static final String blockpath = "bloecke";
	static FileReader blockFileReader;

	public static void main(String[] args) {

		maingui = new JFrame();
		maingui.setSize(650, 450);
		maingui.setTitle("VariedChain V_1.0");
		maingui.setResizable(false);
		maingui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		maingui.setLayout(null);

		yourContent = new JTextField();
		yourContent.setBounds(20, 20, 600, 40);
		yourContent.setVisible(true);
		maingui.add(yourContent);

		btn = new JButton("Create Block");
		btn.setBounds(20, 60, 200, 30);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Block testBlock = new Block();
				testBlock.blockVersion = 2;
				testBlock.hashPayload = null;
				testBlock.hashPriv = null;
				testBlock.payload = yourContent.getText();
				testBlock.payloadType = "text/plain";
				testBlock.timePoint = new Date();
				testBlock.txNum = "1";

				BlockFactory bf = new BlockCreator();

				try {
					bf.addNewBlock(testBlock);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		maingui.add(btn);
		
		
		/*
		for(int i = 0; i<=Database.getSize(); i++) {
		try {
			blockFileReader = new FileReader(blockpath+"/"+i + ".block.json");
			Block block = gson.fromJson(blockFileReader, Block.class);
			blockFileReader.close();
			data[i][0] = block.payload;
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
		// Nullpointerexception bie data array
		*/
		blockchaintabelle = new JTable(data, null);
		blockchaintabelle.setVisible(true);
		blockchaintabelle.setSize(600, 300);

		jScrollPane = new JScrollPane(blockchaintabelle);
		jScrollPane.setBounds(20, 100, 600, 300);
		jScrollPane.setLayout(null);
		jScrollPane.setVisible(true);
		jScrollPane.add(blockchaintabelle);
		maingui.add(jScrollPane);

		maingui.setVisible(true);

	}

}
