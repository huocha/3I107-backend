package io.vertx.db;

import java.io.File;

import io.vertx.structure.Index;
import io.vertx.structure.IndexColumn;
import io.vertx.structure.Table;
import io.vertx.utils.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final String DEFAULT_QUOTE = "'";
    
    private static String workingDirectory = System.getProperty("user.dir");
	private static String dataFileName = "/src/ressource/raw-1.csv";
	protected static Table table = new Table();
	
	protected static ArrayList<IndexColumn> columns = new ArrayList<IndexColumn>();

	protected static File file = new File(workingDirectory+dataFileName); 
	protected static IndexColumn indexCol = new IndexColumn();
	protected static Index newIndex = new Index();
	
	public Parser(Table table) { this.table = table; }
	
    public void parse() throws Exception {
    
        Scanner scanner = new Scanner(file);
        
        String[] firstLine = scanner.nextLine().split(DEFAULT_SEPARATOR); // firstLine is name of columns 
     
        try {
        	while (scanner.hasNext()) {
                Index line = parseLine(scanner.nextLine());
                if(line != null) { table.insert(line); }
            }
        }
        catch(Exception e){
        	Console.error(e);
        }
        
        
        Console.log("-----------");
        
        Console.log("Index: "+table.getIndexes().size());
        Console.log("Count: "+table.count());
        
        List<Index> indexx = table.getIndexes();
        
        Console.log("Index Col: "+ indexx.get(0).getIndexCol().get(0).get("DDS").size() );

        Console.log("Index Col: "+ indexx.get(0).getIndexCol().get(0).get("VTS").size() );
        

        Console.log("Index Col: "+ indexx.get(0).getIndexCol().get(0).get("CMT").size() );
        
        scanner.close();

    }
    
    public static Index parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static Index parseLine(String cvsLine, String separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static Index parseLine(String cvsLine, String separators, String customQuote) {
    	
    	List<String> line = Arrays.asList(cvsLine.split(separators));
		
    	if( line != null && line.size() >= 1 ) {
    	
    		indexCol.insert(line);
    		newIndex.insert(indexCol);
        	
            return newIndex;	
    	}
    	
    	return null;
    	
    }

}

