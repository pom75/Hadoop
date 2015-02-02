package hadoop;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


public class ValeurPoint implements Writable {
	
	private int x;
	private int y;
	private int color;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public ValeurPoint() {
		// TODO Auto-generated constructor stub
	}
	
	public ValeurPoint(int x, int y, int color){
		this.color = color;
		this.x = x;
		this.y = y;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.x = arg0.readInt();
		this.y = arg0.readInt();
		this.color = arg0.readInt();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(this.x);
		arg0.writeInt(this.y);
		arg0.writeInt(this.color);
		
	}


}
