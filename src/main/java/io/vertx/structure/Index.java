package io.vertx.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {

	protected List<IndexColumn> listIndexColumn = new ArrayList<>();
	
	public Index() { this.listIndexColumn = new ArrayList<IndexColumn>(); }
	
	public Index(IndexColumn indexCol) {
		this.listIndexColumn.add(indexCol);
	}
	
	public void insert(IndexColumn indexCol) {
		this.listIndexColumn.add(indexCol);
	}
	
	public List<IndexColumn> getIndexCol() {
		return this.listIndexColumn;
	}
}
