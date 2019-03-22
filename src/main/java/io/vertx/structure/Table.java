package io.vertx.structure;

import java.util.ArrayList;

public class Table {
	protected String tableName;
	protected ArrayList<Column> columns = new ArrayList<Column>();
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
	
	public ArrayList<Column> getFields() {
		return this.columns;
	}
	
	public void addColumn(Column column) {
		this.columns.add(column);
	}
	
	public void addColumns(ArrayList<Column> columns) {
		for(Column col: columns) {
			addColumn(col);
		}
	}
	
	public void insertOne(Row newRow) {
		this.listData.add(newRow);
	}
	
	public void insertMany(ArrayList<Row> newRow) {
		for(Row row: newRow) {
			insertOne(row);
		}
	}
	
	public void showData() {
		for(Row d: listData) {
			System.out.println(d.getKey().toString()+ " - " + d.getValue());
		}
	}
}
