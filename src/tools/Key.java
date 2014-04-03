package tools;

public class Key {
	public static final int SIZE = 32; //as we loop from 0-31 this gives us 32 bytes.
	public static final int MAX_NUM = 40000;

	private byte[] key;


	public Key() {
	this.key = new byte[SIZE];
	}

	public Key(byte[] key) {
		this();
		for(int i = 0; i< Key.SIZE; i++){
			this.setValue(key[i],i);
		}
	}

	public Key(Key k){
		this();
		for(int i = 0; i< Key.SIZE; i++){
			this.setValue(k.getValue(i),i);
		}
	}

	public byte getValue(int index) {
		return key[index];
	}

	public void setValue(byte value, int index){
		this.key[index] = value;
	}

}