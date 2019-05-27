package io.vertx.main;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.utils.Console;

public class MainHttpClient {
	
	public static void main (String [] args) {
		Console.log("Starting main -----");
		
		/**
		 * Get config: current port and other siblings
		 */
		
		String currentPort = System.getProperty("port");
		String[] otherPorts = System.getProperty("other").split(",");
		JsonObject config = new JsonObject();
		config.put("http.port", Integer.parseInt(currentPort));
		config.put("http.otherPorts",  System.getProperty("other"));


		Vertx vertx =  Vertx.vertx();
		
		/**
		 * Deploy server
		 */
		
		DeploymentOptions options = new DeploymentOptions().setConfig(config);
		
		setTimeout(() -> vertx.deployVerticle(MainHttpServer.class, options), 2000);
	
        
	}
	
	public static void setTimeout(Runnable runnable, int delay){
	    new Thread(() -> {
	        try {
	            Thread.sleep(delay);
	            runnable.run();
	        }
	        catch (Exception e){
	            System.err.println(e);
	        }
	    }).start();
	}
}
