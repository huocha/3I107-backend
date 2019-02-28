package structure;

import java.util.HashMap;
import java.util.Map;

public class Index {

	protected Map<Integer, String> index = new HashMap<>();
	protected String value;
	public Index() {}
	
	public Index(String value) {
		this.value = value;
		this.index.put(value.hashCode(), value);
	}

	public String toString() {
		return "{ key:"+ this.value.hashCode()+","+ "value:"+this.index.get(this.value.hashCode()) +" }";
	}
}
