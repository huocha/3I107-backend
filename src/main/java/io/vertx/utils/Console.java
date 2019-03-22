package io.vertx.utils;

public class Console {
	public static void log(String string) { System.out.println(string); }
	
	public static void error(Exception e) {
		log("Error: " + e.getMessage());
		
		e.printStackTrace();
		
	}
}
