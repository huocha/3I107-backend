package io.vertx.main;

import io.vertx.db.Parser;
import io.vertx.structure.Index;
import io.vertx.structure.Table;
import io.vertx.utils.Console;

class Slave extends Thread {

	private Index sharedResource;
	private boolean done = false;
	private Parser parse;
	
	protected int port;
	
	public void halt() {
		done = true;
	}
	
	public Slave(int port) {
		this.port = port;
	}
	
	public void parseLine(Parser parser, Table table, String currentLine) {
		Console.log(table.count()+"");
		parser.parseWithPort(currentLine, table);
	}
	
    public void getByKey() {
     
    }
}
