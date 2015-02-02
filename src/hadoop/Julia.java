package hadoop;

import tools.*;
import ihm.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Julia {

	public static void mainJulia(String fichierIn, String fichierOut, int x, int y, int color, float real,float imag) throws Exception {
		


		Configuration conf = new Configuration();
		conf.setBoolean("mapreduce.map.speculative", false);
		conf.setBoolean("mapreduce.reduce.speculative", false);
		
		//On stock les variable "global"
		conf.setInt("x", x);
		conf.setInt("y", y);
		conf.setFloat("r", real);
		conf.setFloat("i", imag);
		
		creeFichier(x,y,fichierIn); //On créé un fichier avec x*y point pour les transmettre aux map
		Runtime run = Runtime.getRuntime();
		//Process proc2 = run.exec(new String[]{"/bin/sh", "-c", "hdfs dfs -rmr ./"});
		//proc2.waitFor();
		Process proc = run.exec(new String[]{"/bin/sh", "-c", "hdfs dfs -put -f ./"+fichierIn+" /"});
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
		FileInputFormat.addInputPath(job, new Path("/"+fichierIn));
		final Path outDir = new Path(fichierOut);
		FileOutputFormat.setOutputPath(job, outDir);
		final FileSystem fs = FileSystem.get(conf);//récupération d'une référence sur le système de fichier HDFS
		if (fs.exists(outDir)) {
			fs.delete(outDir, true);
		}
		
		
		//System.exit(job.waitForCompletion(true) ? 0 : 1);
		job.waitForCompletion(true);
		//On attends que l'utilisateur quitte la pic pour arreter le programe
		
		
	}

	//Algo de julia
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
		return 256-1;
	}
	
	
	/*
	 * Methode qui crée un fichier de taille x y, a l'endroit ./ , contenant 
	 * x*y ligne de coordonné de point de 0 0 à x y
	 */
	public static void creeFichier(int x,int y,String name){
	       final File fichier =new File("./"+name); 
	       try {
	           // Creation du fichier
	           fichier.createNewFile();
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

	
}
