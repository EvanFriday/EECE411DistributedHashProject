package tools;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Node{
	private InetAddress address;
	private int position;
	private Boolean alive;
	private List<Node> children;
	private Map<Key,Value> kvpairs;
	
	/*
	 * Constructors:
	 */
	public Node(int position,InetAddress address,Boolean alive, List<Node> children, Map<Key,Value> kvpairs){
		this();
		this.position = position;
		this.address = address;
		this.alive = alive;
		this.children = children;
		this.kvpairs = kvpairs;
	}
	public Node(int position,InetAddress address,Boolean alive){
		this();
		this.position = position;
		this.address = address;
		this.alive = alive;
	}	
	public Node(Node node){
		this(node.getPosition(),node.getAddress(),node.getAlive(),node.getChildren(),node.getKvpairs());
	}
	public Node(){
		this.children = new ArrayList<Node>();
		this.kvpairs = new ConcurrentHashMap<Key,Value>();
		this.address = IpTools.getInet();
		this.alive = true;
	}
	
	/*
	 * Mutators
	 */	
	public InetAddress getAddress(){
		return this.address;
	}
	public InetAddress getAddressAt(int index){
		return this.children.get(index).getAddress();
	}
	public void addChild(Node node){
		this.children.add(node);
	}
	public void removeChild(Node node){
		this.children.remove(node);
	}
	public Node getChild(int index){
		return this.children.get(index);
	}
	public void setChildren(List<Node> children){
		this.children = children;
	}
	public List<Node> getChildren(){
		return this.children;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Boolean getAlive() {
		return alive;
	}
	public void setAlive(Boolean alive) {
		this.alive = alive;
	}
	public Map<Key,Value> getKvpairs() {
		return kvpairs;
	}
	public void setKvpairs(Map<Key,Value> kvpairs) {
		this.kvpairs = kvpairs;
	}
	public ErrorCode addToKvpairs(Key k, Value v) {
		if(this.kvpairs.size()<40000){
			this.kvpairs.put(k, v);
			
			if(this.kvpairs.get(k)==v)
				return ErrorCode.OK;
			else
				return ErrorCode.KVSTORE_FAIL;
		}
		else
			return ErrorCode.OUT_OF_SPACE;
	}
	public EVpair getValueFromKvpairs(Key k) {
		EVpair pair;
		Boolean matchfound;
		Value retval = new Value();
		ErrorCode err = ErrorCode.KEY_DNE;
		for(Key ks : this.kvpairs.keySet()){
			matchfound = true;
			
			for(int index = 0; index < 32; index++){
				if (ks.key[index] != k.key[index]){
					matchfound = false;
					break;
				}		
			}
			if(matchfound){
				for(int index = 0; index < Value.SIZE; index++){
					retval.value[index] = this.kvpairs.get(ks).value[index];
				}
				err = ErrorCode.OK;
				break;
			}
		}
		pair = new EVpair(err,retval);
		return pair;
	}
	public ErrorCode removeKeyFromKvpairs(Key k) {
		boolean debug = true;
		boolean matchfound;
		boolean containsKey = this.kvpairs.containsKey(k);
		if(debug) Tools.print("[debug] removeFromKvpairs: containsKey: "+containsKey);
		if(debug) Tools.printByte(k.key);
		
		/*
		if(containsKey) {
			this.kvpairs.remove(k);
			if(this.kvpairs.containsKey(k) == false)
				return ErrorCode.OK;
			else
				return ErrorCode.KVSTORE_FAIL;
		}
		else
			return ErrorCode.KEY_DNE;
		*/
		
		for(Key ks : this.kvpairs.keySet()){
			matchfound = true;
			
			if(debug) Tools.print("[debug] removeFromKvpairs: Key:");
			if(debug) Tools.printByte(ks.key);
			for(int index = 0; index < 32; index++){
				if (ks.key[index] != k.key[index]){
					matchfound = false;
					break;
				}		
			}
			if(matchfound){
				if(debug) Tools.print("[debug] removeFromKvpairs: Match found");
				if(this.kvpairs.remove(ks) != null) {
					return ErrorCode.OK;
				}
			}
		}
		return ErrorCode.KEY_DNE;
	}
}