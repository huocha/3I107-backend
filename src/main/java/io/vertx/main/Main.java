package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class Main {
	
	public static void main (String [] args) {
		Console.log("Starting main -----");
		String currentPort = System.getProperty("key");
		String otherServers = System.getProperty("other");

		Console.log(currentPort+ "--" + otherServers);
		
		Vertx vertx = Vertx.vertx();
		int port = Integer.parseInt(currentPort);
		
		Console.log(port+"");
		
		DeploymentOptions options = new DeploymentOptions()
		    .setConfig(new JsonObject().put("http.port", port)
		);
		
        vertx.deployVerticle(MainVerticle.class, options);
	}
}
