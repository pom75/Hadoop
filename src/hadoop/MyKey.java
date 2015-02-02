package hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class MyKey implements WritableComparable<MyKey>{
	private int direction;
	
	public MyKey(){
		this.direction = -1;
	}
	
	public MyKey(int direction){
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		this.direction = arg0.readInt();
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		arg0.writeInt(this.direction);
		
	}

	@Override
	public int compareTo(MyKey o) {
		// TODO Auto-generated method stub
		if(this.direction < o.direction)
			return -1;
		else if(this.direction == o.direction)
			return 0;
		else
			return 1;
	}

	public boolean equals(Object o) {
		if (!(o instanceof MyKey)) {
			return false;
		}
		
		MyKey other = (MyKey) o;
		return ((this.direction == other.direction));
	}
	
	public String toString(){
		return ""+this.direction;
	}

}
