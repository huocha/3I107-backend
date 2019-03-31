package io.vertx.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table {
	protected List<Index> index = new ArrayList<>();
	
	public Table() { this.index = new ArrayList<Index>(); }
	
	public Table(Index index) {
		this.index.add(index);
	}
	
	public void insert(Index index) {
		this.index.add(index);
	}
	
	public int count() {
		return this.index.size();
	}
	
	public List<Index> getIndexes(){
		return this.index;
	}
	
	
}
