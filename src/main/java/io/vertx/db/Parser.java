package io.vertx.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import io.vertx.structure.Table;
import io.vertx.utils.Console;


// to parser csv to data
public class Parser {
	private String workingDirectory = System.getProperty("user.dir");
	private String dataFileName = "/ressource/raw.csv";
	
	protected Table table;
	protected File file = new File(workingDirectory+dataFileName); 
	protected int totalEntryCount = 0;
	
	public Parser(Table table) {
		this.table = table;
	}
	
	public final void parse() {
		try {
	        BufferedReader reader = new BufferedReader(new FileReader(file));

	        // Reading first line..
	        String[] names = reader.readLine().split(",");
	        Console.log("hello");
	        reader.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}