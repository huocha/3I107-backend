package io.vertx.main;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.utils.Console;

public class Main {
	
	public static void main (String [] args) {
		Console.log("Starting main -----");
		
		String currentPort = System.getProperty("port");
		String otherPorts = System.getProperty("other");
		
		Vertx vertx = Vertx.vertx();

		JsonObject config = new JsonObject();
		config.put("http.port", Integer.parseInt(currentPort));
		config.put("http.otherPorts", otherPorts);
		
		DeploymentOptions options = new DeploymentOptions().setConfig(config);
		
        vertx.deployVerticle(MainVerticle.class, options);
	}
}
