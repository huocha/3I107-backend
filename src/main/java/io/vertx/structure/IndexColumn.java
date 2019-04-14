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
import io.vertx.utils.Console;

public class IndexColumn {
	
	protected Map<String, List<Integer>> index = new HashMap<String, List<Integer>>();
	protected int nbLines ;
	protected int idCol; //index by number of Column
	
	
	public IndexColumn(int n) {
		this.idCol = n;
		this.nbLines = 1; 
	}
	
	public Map<String, List<Integer>> getIndex() {
		return index;
	}

	public void setIndex(Map<String, List<Integer>> index) {
		this.index = index;
	}
	
	public int getNbLines() {
		return nbLines;
	}
	
	public int getIdColumn() {
		return idCol;
	}

	public void insert(List<String> line) {
		
		// position of current line
		int nbLine = this.nbLines;
		
		// Eg: VTS, ...
		String keyName = line.get(idCol);
		
		// list of position of key name Eg: <1, 5, 9> 
		List<Integer> rows = index.get(keyName);
		
		if(rows == null) {
			rows = new ArrayList<Integer>();
			index.put(keyName, rows);
		}
		
		rows.add(nbLine);
		this.nbLines++;

	}

	public List<List<String>> get(String searchedKey) throws IOException{
		List<Integer> rows = index.get(searchedKey);
		
		if (rows == null) { return null; }
		
		List<List<String>> result = new ArrayList<>();
		int tmp=0;

		String fileParsed = Parser.getWorkingDirectory()+Parser.getDataFileSearch();
		try (BufferedReader reader = Files.newBufferedReader(
		        Paths.get(fileParsed), StandardCharsets.UTF_8)) {
			
			for(Integer row: rows) {
				List<String> line = reader.lines()
					 					.skip(row-tmp-1)
                     					.limit(1)
                     					.collect(Collectors.toList());
				result.add(line);
				tmp=row;
			}
		 
		}
		catch(Exception e) { Console.error(e); }
		
	
		
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
