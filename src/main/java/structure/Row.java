package structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Row: Each row = <hash, list of string> correspond the nb of column
// Operation: SELECT/INSERT/UPDATE/DELETE

public class Row {
	protected Map<Index,ArrayList<String>> data = new HashMap<>();
	protected Index key;
	protected ArrayList<String> values;
	
	public Row(Index key, ArrayList<String> values) { 
		this.key = key;
		this.values = values;
		this.data.put(key,values);
	}
	
	
	public Index getKey() {
		return this.key;
	}
	
	public ArrayList<String> getValue() {
		return this.values;
	}
	
	public ArrayList<String> getValue(Index key) {
		return data.get(key);
	}
	
}
