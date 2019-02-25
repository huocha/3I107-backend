package io.vertx.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
	protected String tableName;
	protected ArrayList<Field> fields;
	protected Index index;
	protected ArrayList<Data> listData = new ArrayList<Data>();
	
	public Table() {}
	public Table(String tableName, ArrayList<Field> fields) {
		this.tableName = tableName;
		this.fields = fields; 
		this.listData = listData;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public Index getIndex() {
		return this.index;
	}
	
	public void addIndex(String columnName) {
		for(Field f : fields) {
			if (f.getName().equals(columnName)) {
				f.setIndex();
			}
		}
	}
	
	public ArrayList<Field> getFields() {
		return this.fields;
	}
	
	public int getPositionIndex() {
		int pos = 0;
		for(Field f : fields) {
			if (f.isIndex()) { pos = fields.indexOf(f); return pos;}
		}
		return pos;
	}
	
	public void insertOne(ArrayList<String> documents) {
		int position = getPositionIndex();
		
		Index index = new Index(documents.get(position));
		
		Data newRow = new Data(index, documents);
			
		this.listData.add(newRow);
	}
	
	public void showData() {
		for(Data d: listData) {
			System.out.println(d.getKey().toString()+ " - " + d.getValue());
		}
	}
}
