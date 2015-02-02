package hadoop;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class MyPartitioner extends Partitioner<MyKey,ValeurPoint> {

	@Override
	public int getPartition(MyKey arg0, ValeurPoint arg1, int arg2) {
		//si le nombre de reduce est de 1
		if(arg2 == 0){
			return 0;
		}
		
		if(arg0.getDirection() >= 0){
			return 0;
		}else{
			return 1;
		}
	}

}
