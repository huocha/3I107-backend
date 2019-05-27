package io.vertx.main;

import java.util.List;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.db.Parser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class MainHttpServer extends AbstractVerticle {
	private String[] otherPorts;
	private int port;
	Parser parser, parser1, parser2;
	public void start() throws Exception {
		otherPorts = this.context.config().getString("http.otherPorts").split(",");
		port = this.context.config().getInteger("http.port", 8080);
		
		parser = new Parser(port);
		parser1 = new Parser(Integer.parseInt(otherPorts[0]));
		parser2 = new Parser(Integer.parseInt(otherPorts[1]));
		
		Router router = Router.router(vertx);
		router.get("/getFromCurrentPort/").handler(this::getFromCurrentPort);
		router.get("/getFromOtherPorts/").handler(this::getFromOtherPorts);
		router.get("/get/").handler(this::get);
		
		router.post("/postData/").handler(this::sendData);
		
		vertx.createHttpServer()
			.requestHandler(router::accept)
			.listen(port,
	          result -> {
	            if (result.succeeded()) {
	              Console.log("Running in: "+ result.result().actualPort());
	             
	            } else {
	              Console.log("ERROR "+ result.cause());
	            }
	          }
	      );
		
		
    }
	
	private void getFromCurrentPort(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		try {
			parser.parse(0);
			List<List<String>> result_VTS = parser.getTable().getIndexes().get(0).getIndexCol().get(0).get("VTS")/*.size()*/;
			JsonObject currentData = new JsonObject();
			currentData.put("result_"+port, result_VTS);
			
			response.putHeader("Content-Type", "application/json").setChunked(true);
			response.write(currentData.encode());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.write(e.getMessage());
			response.setStatusCode(500);
		} 
		
		response.end();
		
	}
	

	private Future<JsonObject> getFromPort(int port) {
	    Future<JsonObject> future = Future.future();
	    
	    HttpClientOptions requestOptions = new HttpClientOptions()
				.setDefaultHost("localhost")
				.setDefaultPort(port)
				.setLogActivity(true);
	    HttpClient client = vertx.createHttpClient(requestOptions);
	    
	    client.request(HttpMethod.GET, "/getFromCurrentPort", clientReponse -> {
			Console.log("Received response with status code " + clientReponse.statusCode());
			
			clientReponse.bodyHandler(body -> {
	            future.complete(body.toJsonObject());
	        });
	  
		}).exceptionHandler(e -> {
			future.fail(e.toString());
    	}).end();
	    
	    return future;
	}
	

	private void getFromOtherPorts(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		Future<JsonObject> getOtherPort1 = getFromPort(Integer.parseInt(otherPorts[0]));
		Future<JsonObject> getOtherPort2 = getFromPort(Integer.parseInt(otherPorts[1]));

		// simultaneously get data from 2 sibling ports
		CompositeFuture.join(getOtherPort1, getOtherPort2).setHandler(ar -> {
		  if (ar.succeeded()) {
			  
			  JsonObject result1 = ar.result().resultAt(0);
			  JsonObject combined = result1.mergeIn(ar.result().resultAt(1));
			  
			  response.end(combined.encodePrettily());
		    
		  } else {
		    // At least one server failed
			  response.end("err"); 
		  }
		});

	}
	
	private void get(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		HttpClientOptions requestOptions = new HttpClientOptions()
				.setDefaultHost("localhost")
				.setDefaultPort(port)
				.setLogActivity(true);
		
		HttpClient client = vertx.createHttpClient(requestOptions);
		
		/**
		 * Simple request without body 
		 */
		JsonObject currentData = new JsonObject();
			
		try {
			parser.parse(0);
			List<List<String>> result_VTS = parser.getTable().getIndexes().get(0).getIndexCol().get(0).get("VTS")/*.size()*/;
			
			currentData.put("result_"+port, result_VTS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.request(HttpMethod.GET, "/getFromOtherPorts", clientReponse -> {
			System.out.println("Received response with status code @" + clientReponse.statusCode());
			
			
			clientReponse.bodyHandler(body -> {
				JsonObject bodyResponse = body.toJsonObject();
				
				response.putHeader("Content-Type", "application/json").setChunked(true);
				
				response.write(bodyResponse.encodePrettily());
				response.write(currentData.encodePrettily());
				
				response.end();
			});
		}).end();
		
	}
	
	private void sendData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		response.end();
	}

}
