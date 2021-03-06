package main;

import ihm.MainPanel;

import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.hadoop.util.GenericOptionsParser;

import tools.MakeSound;

public class Main {
	
	static public String in ="";
	static public String out ="";
	
	public static void main(String args[]) throws IOException{
		
		String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: julia <in> <out> ");
			System.exit(2);
		}
		
		//nom du fichier d'entré 
		in = otherArgs[0];
		//Nom du fichier de sortie
		out = otherArgs[1];
	
		JFrame f = new JFrame();
		f.add(new MainPanel());
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		
		
		new MakeSound().playSound();
		f.show();
	}
}
