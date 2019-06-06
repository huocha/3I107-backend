package io.vertx.operation;

import java.util.Arrays;
import java.util.HashMap;

import io.vertx.db.Parser;
import io.vertx.utils.Console;


public class Query {
	String select, where; 
	String [] allCol = Parser.firstLine.split(",");
	public Query(String select, String where) {
		this.select = select;
		this.where = where;
	}
	
	public HashMap<Integer, String> parseQuery() {
		String[] allConditions = this.where.split(",");
		HashMap<Integer, String> result = new HashMap<Integer, String>();

		for( String condition: allConditions) {
			String key = condition.split("=")[0];
			int index = Arrays.asList(allCol).indexOf(key.toUpperCase());

			if(index !=-1) {
				result.put(index, condition.split("=")[1]);
			}
		}
		return result;
	}
}
