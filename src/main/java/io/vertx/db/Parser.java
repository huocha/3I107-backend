package io.vertx.db;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import io.vertx.core.json.JsonObject;
import io.vertx.structure.Index;
import io.vertx.structure.IndexColumn;
import io.vertx.structure.Table;
import io.vertx.utils.Console;
import io.vertx.utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.rmi.CORBA.Util;

public class Parser {

	private static final String DEFAULT_SEPARATOR = ",";
	private static final String DEFAULT_QUOTE = "'";
	public static final String ROOT = "/ressource/";
	
	private int numPort;
	private static String workingDirectory = System.getProperty("user.dir");
	private static String dataFileName = ROOT+"rawTest0.csv"; //file source
	public static String dataFileSearch; //new file to be searched data

	protected Table table;

	protected static ArrayList<IndexColumn> columns = new ArrayList<IndexColumn>();

	protected static File file = new File(workingDirectory+dataFileName);

	protected static Index newIndex = new Index();
	
	public static String firstLine;
	public Parser(int port) { 
		this.table = new Table(); 
		this.numPort=port%10;
		this.dataFileSearch = ROOT+"rawTest"+this.numPort+".txt";
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public static String getDataFileSearch() {
		return dataFileSearch;
	}

	public static String getWorkingDirectory() {
		return workingDirectory;
	}

	public static String getDataFileName() {
		return dataFileName;
	}
	
	public static String getColName() {
		return firstLine;
	}
	
	public void parseWithPort(String line, Table table) {
		IndexColumn firstColumn = new IndexColumn(0); // IndexColumn by the first column->numero 0 , ie: vendor_name
        Index indexFirstColumn = new Index();
        indexFirstColumn.insert(firstColumn);
		
        parseLine(line, indexFirstColumn.getIndexCol().get(0));
		
		table.insert(indexFirstColumn);
		
	}
	
    public void parse(int numColumnToIndex) throws Exception {

        Scanner scanner = new Scanner(file);

        Parser.firstLine = scanner.nextLine().toUpperCase();/*.split(DEFAULT_SEPARATOR);*/ // firstLine is name of columns
        
        scanner.nextLine(); //pass the line with ","

        IndexColumn firstColumn = new IndexColumn(numColumnToIndex); //IndexColumn by the numero : numColumnToIndex column->numero 0 , ie: vendor_name
        Index indexFirstColumn = new Index();
        indexFirstColumn.insert(firstColumn);

        File fileSearch = new File(workingDirectory+Parser.getDataFileSearch());
        boolean fileIsExisted = fileSearch.exists(); //check if the file research existe?
        PrintWriter  pw=  null;

        if (!fileIsExisted) {
			fileSearch.createNewFile();
			pw = new PrintWriter(workingDirectory+dataFileSearch);
        }

        try {
        	int numLigne=0;
        	while (scanner.hasNext()) {
        		numLigne=(numLigne%3) +1;
        		String thisLine = scanner.nextLine();
        		
        		if (numLigne==this.numPort) { //numero line correspond numero port
	        		
	        		if (!fileIsExisted) pw.println(thisLine); //write new file
	
	        		parseLine(thisLine, indexFirstColumn.getIndexCol().get(0));
        		}

            }
        }
        catch(Exception e){ Console.error(e); }
        finally { if (pw!=null) pw.close(); }

        table.insert(indexFirstColumn); // add index which contain indexColumn of vendor_name to table for ex


        scanner.close();

    }


    public static void parseLine(String cvsLine, IndexColumn ic) {
         parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE, ic);
    }


    public static void parseLine(String cvsLine, String separators, String customQuote, IndexColumn indexColumn) {

    	List<String> line = Arrays.asList(cvsLine.split(separators));

    	if( line != null && line.size() >= 1 ) {
    		indexColumn.insert(line);
    	}
    }
    
    
    public JsonObject findMany(HashMap<Integer, String> conds) throws IOException {
    	JsonObject result = new JsonObject();
 
    	List<List<String>> result1 = new ArrayList<>() ;
    	
    	for(Integer key: conds.keySet()) {
    		String value = conds.get(key);
    		if(key==0) {
    			result1.add(this.table.getIndexes().get(0).getIndexCol().get(0).get(value)); 
    		}
    		else {
    			result1.add(this.table.getIndexes().get(0).getIndexCol().get(0).getWithoutIndex(key, value));
    		}
		}
    	
    	List<String> toReturn = Helper.intersection(result1);
    	if(toReturn.size() > 0) {
    		result.put("result_"+numPort, toReturn);
    	}
    	
    	return result;
    }

}
