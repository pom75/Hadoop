import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Julia {
	static public double real = -0.8;
	static public double imag = -0.4;
	public Complex c = new Complex(real, imag); 
	static double xmin   = -2.0;
	static double ymin   = -2.0;
	static double width  =  4.0;
	static double height =  4.0;
	static int x = 1000;
	static int y = 1000;
	static Picture pic;
	static boolean test = true;
	public static List<Integer> colors;


	public static void mainJulia(String fichierIn, String fichierOut, int x, int y, int color, double real,double imag) throws Exception {
		

		colors = getUniqueColors(256);

		Configuration conf = new Configuration();
		conf.setBoolean("mapreduce.map.speculative", false);
		conf.setBoolean("mapreduce.reduce.speculative", false);
		Julia.x = x;
		Julia.y = y;
		creeFichier(x,y,"./preP"); //On créé un fichier avec x*y point pour les transmettre aux map
		Runtime run = Runtime.getRuntime();
		Process proc = run.exec(new String[]{"/bin/sh", "-c", "hdfs dfs -copyFromLocal ./preP /"});
		proc.waitFor();
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		Job job = Job.getInstance(conf, "ens Julia");
		job.setJarByClass(Julia.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(ValeurPoint.class);
		job.setPartitionerClass(MyPartitioner.class);
		job.setNumReduceTasks(1);//Il est bien sur possible de changer cette valeur (1 par défaut)
		FileInputFormat.addInputPath(job, new Path(fichierIn));
		final Path outDir = new Path(fichierOut);
		FileOutputFormat.setOutputPath(job, outDir);
		final FileSystem fs = FileSystem.get(conf);//récupération d'une référence sur le système de fichier HDFS
		if (fs.exists(outDir)) {
			fs.delete(outDir, true);
		}
		//System.exit(job.waitForCompletion(true) ? 0 : 1);
		job.waitForCompletion(true);
		//On attends que l'utilisateur quitte la pic pour arreter le programe
		
		Process proc2 = run.exec(new String[]{"/bin/sh", "-c", "hdfs dfs -copyToLocal /user/Steph/GOZG/part-r-00000 ./"});
		proc2.waitFor();
	}

	public static List<Integer> getUniqueColors(int amount) {
		final int lowerLimit = 0x10;
		final int upperLimit = 0xE0;    
		final int colorStep = (int) ((upperLimit-lowerLimit)/Math.pow(amount,1f/3));

		final List<Integer> colors = new ArrayList<Integer>(amount);

		for (int R = lowerLimit;R < upperLimit; R+=colorStep)
			for (int G = lowerLimit;G < upperLimit; G+=colorStep)
				for (int B = lowerLimit;B < upperLimit; B+=colorStep) {
					if (colors.size() >= amount) { //The calculated step is not very precise, so this safeguard is appropriate
						return colors;
					} else {
						int color = (R<<16)+(G<<8)+(B);
						colors.add(color);
					}               
				}
		return colors;
	}

	public static int julia(double Ac , double Ab , double x , double y){
		Complex c = new Complex(Ac,Ab);
		Complex z = new Complex(x,y);
		for(int i = 0 ; i < 256 ; i++ ){
			Complex fz = (z.times(z)).plus(c);
			double norm = fz.abs();
			if(norm > 2.0){
				return i;
			}else{
				z = fz;
			}
		}
		return 10-1;
	}
	
	public static void creeFichier(int x,int y,String path){
		final String chemin = "C:/tmp.txt";
	       final File fichier =new File(path); 
	       try {
	           // Creation du fichier
	           fichier .createNewFile();
	           // creation d'un writer (un écrivain)
	           final FileWriter writer = new FileWriter(fichier);
	           try {
	        	   for( int i = 0; i<x;i++){
	       			for( int j = 0; j<y;j++){
	       				System.out.println();
	       			 writer.write(i+" "+j+"\n");
	       			}
	       		}
	           } finally {
	               // quoiqu'il arrive, on ferme le fichier
	               writer.close();
	           }
	       } catch (Exception e) {
	           System.out.println("Impossible de creer le fichier");
	       }
	}


	public static class TokenizerMapper 
	extends Mapper<Object, Text, Text, ValeurPoint>{

		private final static ValeurPoint res = new ValeurPoint();
		private Text word = new Text();


		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			
			
			StringTokenizer itr = new StringTokenizer(value.toString());
			int i = Integer.parseInt(itr.nextToken());
			int j = Integer.parseInt(itr.nextToken());
			double x = xmin +  i * width / Julia.x;
			double y = ymin + j * height / Julia.y;
			int julia =  julia(real,imag,x,y);
			//res.set(julia);
			res.setColor(julia);
			res.setX(i);
			res.setY(j);
			word.set("oneKey");
			context.write(word, res);
		}
	}



	public static class IntSumReducer 
	extends Reducer<Text,ValeurPoint,Text,ValeurPoint> {
		private ValeurPoint result = new ValeurPoint();

		public void reduce(Text key, Iterable<ValeurPoint> values, 
				Context context
				) throws IOException, InterruptedException {
			pic = new Picture(Julia.x, Julia.y);
			for (ValeurPoint val : values) {
				pic.set(val.getX(), val.getY(), new Color(colors.get(val.getColor())));
			}
			
			pic.show();
			
			//context.write(key, result); useless pour nous

		}
	}
	
	


}
