package io.vertx.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	public String fileSearch = Parser.getWorkingDirectory()+Parser.getDataFileSearch();
	
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

	public List<String> get(String searchedKey) throws IOException{
		List<Integer> rows = index.get(searchedKey);
		
		if (rows == null) { return null; }
		
		List<String> result = new ArrayList<>();
		int tmp=0;

		try (BufferedReader reader = Files.newBufferedReader(
		        Paths.get(fileSearch), StandardCharsets.UTF_8)) {
			
			for(Integer row: rows) {
				
				List<String> line = reader.lines()
					 					.skip(row-tmp-1)
                     					.limit(1)
                     					.collect(Collectors.toList());
				result.addAll(line);
				tmp=row;
			}
		 
		}
		catch(Exception e) { Console.error(e); }
		
	
		
		return result;
	}
	
	public List<String> getWithoutIndex(int index, String key) throws FileNotFoundException{
		List<String> result = new ArrayList<>();
		File file = new File(fileSearch);
		Scanner scanner = new Scanner(file);
		
		while (scanner.hasNext()) {
     		String thisLine=scanner.nextLine().toUpperCase(); // avoid case sensitive Cash, CASH, cash
     		List<String> line = Arrays.asList(thisLine.split(","));
     		
     		if(index < line.size() && line.get(index).equals(key.toUpperCase())) {
     			result.add(thisLine);
     		}
		}
		
		scanner.close();
		return result;
	}
	
}
