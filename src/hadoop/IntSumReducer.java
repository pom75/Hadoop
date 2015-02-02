package hadoop;

import ihm.Picture;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

	public class IntSumReducer 
	extends Reducer<MyKey,ValeurPoint,MyKey,ValeurPoint> {
		private ValeurPoint result = new ValeurPoint();

		public void reduce(MyKey key, Iterable<ValeurPoint> values, 
				Context context
				) throws IOException, InterruptedException {
			
			//On recuepre les variables "gloables qui nous interesse
			Configuration conf = context.getConfiguration();
			int xMax = conf.getInt("x", 0);
			int yMax = conf.getInt("y", 0);
			int color = conf.getInt("color", 0);
			
			//On crée la liste des couleurs
			List<Integer> colors = getUniqueColors(color);
			
			
			//On crée l'objet picture qui va dessiner l'image
			Picture pic = new Picture(xMax, yMax);
			
			
			//Pour tous les points, on les ajoute a la structure 
			if(key.getDirection() > 0 ){
				for (ValeurPoint val2 : values) {
					pic.set(val2.getX(), val2.getY(), new Color(colors.get(val2.getColor())));
					val2.setColor(colors.get(val2.getColor()));
					context.write(key, val2);
				}
			}else{
				for (ValeurPoint val2 : values) {
					Color c = new Color(colors.get(val2.getColor()));
					int red = (int) (c.getRed() * 0.299);
					int green =(int) (c.getGreen() * 0.587);
					int blue = (int) (c.getBlue() *0.114);
					pic.set(val2.getX(), val2.getY(), new Color(red+green+blue,red+green+blue,red+green+blue));
					val2.setColor(red+green+blue);
					context.write(key, val2);
				}
				
			}
			
			//On affiche le résulat 
			pic.show();
			
			 

		}
		
		//Méthode pour les couleurs
		public List<Integer> getUniqueColors(int amount) {
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
	}