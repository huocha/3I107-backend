package io.vertx.db;

import java.io.File;

import io.vertx.structure.Column;
import io.vertx.structure.Index;
import io.vertx.structure.Row;
import io.vertx.structure.Table;
import io.vertx.utils.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final String DEFAULT_QUOTE = "'";
    
    private static String workingDirectory = System.getProperty("user.dir");
	private static String dataFileName = "/src/ressource/raw.csv";
	protected static Table table = new Table();
	protected static ArrayList<Column> columns = new ArrayList<Column>();
	protected static ArrayList<Row> rows = new ArrayList<Row>();
	protected static File file = new File(workingDirectory+dataFileName); 
	
	public Parser() { this.table = table; }
	
    public static Table parse() throws Exception {
    
        Scanner scanner = new Scanner(file);
        
        String[] firstLine = scanner.nextLine().split(DEFAULT_SEPARATOR); // firstLine is name of columns 
        
        for(String str: firstLine) {
        	Column newColumn = new Column(str);
        	columns.add(newColumn);
        }
        
        while (scanner.hasNext()) {
            Row line = parseLine(scanner.nextLine());
            rows.add(line);
        }
        
        table.insertMany(rows);
        
        // end test
        
        scanner.close();
        
        return table;

    }
    
    public static Row parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static Row parseLine(String cvsLine, String separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static Row parseLine(String cvsLine, String separators, String customQuote) {
    	
    	ArrayList<String> line = new ArrayList(Arrays.asList(cvsLine.split(separators)));
    	
    	Index key = new Index(line.get(0));
    	
    	Row newRow = new Row(key, line);
    	
        return newRow;
    }

}

