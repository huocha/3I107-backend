package io.vertx.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexColumn {
	
	protected Map<String, List<Integer>> index = new HashMap<String, List<Integer>>();
	protected List<List<String>> lines = new ArrayList<>();
	
	public IndexColumn() {}
	
	public void insert(List<String> line) {
		
		int nbLine = lines.size();
		
		lines.add(line);
		
		List<Integer> rows = index.get(line.get(0));
		
		if(rows == null) {
			rows = new ArrayList<Integer>();
			index.put(line.get(0), rows);
		}
		
		rows.add(nbLine);

	}
	
	public List<List<String>> get(String key){
		List<Integer> rows = index.get(key);
		if (rows == null) { return null; }
		List<List<String>> result = new ArrayList<>();
		
		for(Integer row: rows) {
			result.add(lines.get(row));
		}
		return result;
	}
	
	public List<List<String>> getWithoutIndex(String key){
		List<List<String>> result = new ArrayList<>();
		
		for(List<String> line: lines) {
			if (line.get(0).equals(key)) {
				result.add(line);
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "IndexColumn [index=" + index.toString() + ", lines=" + lines.get(0) + "]";
	}

}
