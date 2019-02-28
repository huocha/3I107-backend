package structure;

import java.util.ArrayList;

public class Table {
	protected String tableName;
	protected ArrayList<Column> columns;
	protected Index index;
	protected ArrayList<Row> listData = new ArrayList<Row>();
	
	public Table() {}
	public Table(String tableName, ArrayList<Column> columns) {
		this.tableName = tableName;
		this.columns = columns; 
		this.listData = listData;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public Index getIndex() {
		return this.index;
	}
	
	public void addIndex(String columnName) {
		for(Column f : columns) {
			if (f.getName().equals(columnName)) {
				f.setIndex();
			}
		}
	}
	
	public ArrayList<Column> getFields() {
		return this.columns;
	}
	
	public int getPositionIndex() {
		int pos = 0;
		for(Column f : columns) {
			if (f.isIndex()) { pos = columns.indexOf(f); return pos;}
		}
		return pos;
	}
	
	public void insertOne(ArrayList<String> documents) {
		int position = getPositionIndex();
		
		Index index = new Index(documents.get(position));
		
		Row newRow = new Row(index, documents);
			
		this.listData.add(newRow);
	}
	
	public void showData() {
		for(Row d: listData) {
			System.out.println(d.getKey().toString()+ " - " + d.getValue());
		}
	}
}
