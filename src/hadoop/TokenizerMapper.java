package hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class TokenizerMapper extends Mapper<Object, Text, Text, ValeurPoint>{
	static double xmin   = -2.0;
	static double ymin   = -2.0;
	static double width  =  4.0;
	static double height =  4.0;
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
		int julia =  Julia.julia(real,imag,x,y);

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