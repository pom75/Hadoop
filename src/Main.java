
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.hadoop.util.GenericOptionsParser;

public class Main {
	
	static String in ="";
	static String out ="";
	
	public static void main(String args[]) throws IOException{
		
		String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: julia <in> <out> ");
			System.exit(2);
		}
		
		in = otherArgs[0];
		out = otherArgs[1];
		
		JFrame f = new JFrame();
		f.add(new MainPanel());
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.show();
	}
}
