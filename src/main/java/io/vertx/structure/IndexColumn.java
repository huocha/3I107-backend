package io.vertx.structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import io.vertx.db.Parser;

public class IndexColumn {
	
	protected Map<String, List<Integer>> index = new HashMap<String, List<Integer>>();
	public Map<String, List<Integer>> getIndex() {
		return index;
	}


	public void setIndex(Map<String, List<Integer>> index) {
		this.index = index;
	}

	protected int nbLines;
	protected int numColumn; //index by number of Column
	
	public IndexColumn(int n) {
		this.numColumn=n;
		this.nbLines=1; 
		
	}
	
	
	public int getNbLines() {
		return nbLines;
	}


	public void setNbLines(int nbLines) {
		this.nbLines = nbLines;
	}


	public void insert(List<String> line) {
		
		int nbLine = this.nbLines;
		
		List<Integer> rows = index.get(line.get(numColumn));
		
		if(rows == null) {
			rows = new ArrayList<Integer>();
			index.put(line.get(numColumn), rows);
		}
		
		rows.add(nbLine);
		this.nbLines++;

	}
	
	public int getNumColumn() {
		return numColumn;
	}

	public void setNumColumn(int numColumn) {
		this.numColumn = numColumn;
	}

	public List<List<String>> get(String key) throws IOException{
		List<Integer> rows = index.get(key);
		if (rows == null) { return null; }
		List<List<String>> result = new ArrayList<>();
		int tmp=0;
		try (BufferedReader reader = Files.newBufferedReader(
		        Paths.get(Parser.getWorkingDirectory()+Parser.getDataFileSearch()), StandardCharsets.UTF_8)) {
			for(Integer row: rows) {
				 List<String> line = reader.lines()
                         .skip(row-tmp-1)
                         .limit(1)
                         .collect(Collectors.toList());
				result.add(line);
				tmp=row;
			}
		 
		}
	
		
		return result;
	}
	
	public List<List<String>> getWithoutIndex(String key){
		List<List<String>> result = new ArrayList<>();
		 Scanner scanner = new Scanner(Parser.getWorkingDirectory()+Parser.getDataFileSearch());
		 while (scanner.hasNext()) {
     		String thisLine=scanner.nextLine();
     		List<String> line = Arrays.asList(thisLine.split(","));
			if (line.get(0).equals(key)) {
				result.add(line);
			}
		}
		 scanner.close();
		return result;
	}
	
}
