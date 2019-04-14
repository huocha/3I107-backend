package io.vertx.db;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

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
	private static String dataFileName = "/src/ressource/rawTest.csv"; //file source
	private static String dataFileSearch = "/src/ressource/rawTest.txt"; //new file to be searched data

	protected static Table table = new Table();

	protected static ArrayList<IndexColumn> columns = new ArrayList<IndexColumn>();

	protected static File file = new File(workingDirectory+dataFileName);
	//protected static IndexColumn indexCol = new IndexColumn();
	protected static Index newIndex = new Index();

	public Parser(Table table) { this.table = table; }
	
	public static String getDataFileSearch() {
		return dataFileSearch;
	}

	public static String getWorkingDirectory() {
		return workingDirectory;
	}

	public static String getDataFileName() {
		return dataFileName;
	}
	
	public void parseWithPort(String line, Table table) {
		IndexColumn firstColumn = new IndexColumn(0); // IndexColumn by the first column->numero 0 , ie: vendor_name
        Index indexFirstColumn = new Index();
        indexFirstColumn.insert(firstColumn);
		
        parseLine(line, indexFirstColumn.getIndexCol().get(0));
		
		table.insert(indexFirstColumn);
		
	}
	
    public void parse() throws Exception {

        Scanner scanner = new Scanner(file);

        String[] firstLine = scanner.nextLine().split(DEFAULT_SEPARATOR); // firstLine is name of columns

        scanner.nextLine();//pass the line with ","

        IndexColumn firstColumn = new IndexColumn(0); // IndexColumn by the first column->numero 0 , ie: vendor_name
        Index indexFirstColumn = new Index();
        indexFirstColumn.insert(firstColumn);

        File fileSearch = new File(workingDirectory+dataFileSearch);
        boolean fileIsExisted = fileSearch.exists(); //check if the file research existe?
        PrintWriter  pw=  null;

        if (!fileIsExisted) {
			fileSearch.createNewFile();
			pw = new PrintWriter(workingDirectory+dataFileSearch);
        }

        try {
        	while (scanner.hasNext()) {
        		String thisLine = scanner.nextLine();

        		if (!fileIsExisted) pw.println(thisLine); //write new file

        		parseLine(thisLine, indexFirstColumn.getIndexCol().get(0));

            }
        }
        catch(Exception e){ Console.error(e); }
        finally { pw.close(); }

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
    /*
    public static Index parseLine(String cvsLine,  int numColumn) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE, numColumn);
    }

    public static Index parseLine(String cvsLine, String separators, int numColumn) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE, numColumn);
    }

    public static Index parseLine(String cvsLine, String separators, String customQuote,  int numColumn) {

    	List<String> line = Arrays.asList(cvsLine.split(separators));

    	if( line != null && line.size() >= 1 ) {

    		indexCol.insert(line);
    		newIndex.insert(indexCol);

            return newIndex;
    	}

    	return null;

    }*/

}
