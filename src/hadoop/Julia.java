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
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Julia {
	static double xmin   = -2.0;
	static double ymin   = -2.0;
	static double width  =  4.0;
	static double height =  4.0;

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

	//Méthode pour les couleurs
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


	// Class du map ! 
	public static class TokenizerMapper 
	extends Mapper<Object, Text, Text, ValeurPoint>{

		private final static ValeurPoint res = new ValeurPoint();
		private Text word = new Text();


		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			
			
			
			// On parse la ligne du map 
			StringTokenizer itr = new StringTokenizer(value.toString());
			// point de coordoné X
			int i = Integer.parseInt(itr.nextToken());
			// point de coordoné Y
			int j = Integer.parseInt(itr.nextToken());
			
			
			//On recupere les variable "global"
			Configuration conf = context.getConfiguration();
			float real = conf.getFloat("r", 0);
			float imag = conf.getFloat("i", 0);
			int xMax = conf.getInt("x", 0);
			int yMax = conf.getInt("y", 0);
			
			//On calcul la couleur du point a l'aide de l'algo
			double x = xmin +  i * width / xMax;
			double y = ymin + j * height / yMax;
			int julia =  julia(real,imag,x,y);

			//On stoque le résultat dans un objet qui implements Writable
			res.setColor(julia);
			res.setX(i);
			res.setY(j);
			
			//On met la meme clé pour que que toutes les valeurs aillent dans 
			//le meme réduce (pseudo distribued) 
			word.set("oneKey");
			context.write(word, res);
		}
	}


	
	//Classe du réduce
	public static class IntSumReducer 
	extends Reducer<Text,ValeurPoint,Text,ValeurPoint> {
		private ValeurPoint result = new ValeurPoint();

		public void reduce(Text key, Iterable<ValeurPoint> values, 
				Context context
				) throws IOException, InterruptedException {
			
			//On recuepre les variables "gloables qui nous interesse
			Configuration conf = context.getConfiguration();
			int xMax = conf.getInt("x", 0);
			int yMax = conf.getInt("y", 0);
			
			//On crée la liste des couleurs
			List<Integer> colors = getUniqueColors(256);
			
			
			//On crée l'objet picture qui va dessiner l'image
			Picture pic = new Picture(xMax, yMax);
			
			
			//Pour tous les points, on les ajoute a la structure 
			for (ValeurPoint val2 : values) {
				pic.set(val2.getX(), val2.getY(), new Color(colors.get(val2.getColor())));
			}
			
			//On affiche le résulat 
			pic.show();
			
			//context.write(key, result); useless pour nous d'écrir un fichier avec le resu

		}
	}
	
}
