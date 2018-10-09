
public class PriorityNode<Key, Value> {
	private Key key;
	private Value value;
	
	public PriorityNode(Key k, Value v) {
		this.key = k;
		this.value = v;
	}
	
	public Key key() {
		return this.key;
	}
	
	public Value value() {
		return this.value;
	}
	
	public Key setKey(Key k) {
		this.key = k;
		return this.key;
	}
	
	public Value setValue(Value v) {
		this.value = v;
		return this.value;
	}
}
