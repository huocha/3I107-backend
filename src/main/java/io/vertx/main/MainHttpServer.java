package io.vertx.main;

import java.util.List;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
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
	Parser parser;
	public void start() throws Exception {
		otherPorts = this.context.config().getString("http.otherPorts").split(",");
		port = this.context.config().getInteger("http.port", 8080);
		
		parser = new Parser(port);
		
		vertx.executeBlocking(future -> {

			// Do the blocking operation in here
			try { parser.parse(0); } 
			catch (Exception ignore) { }
			
			future.complete("finish parser");
        
		}, res -> {

			if (res.succeeded()) {
				Console.log(res.result().toString());
			} else {
				res.cause().printStackTrace();
			}
        
		});
		
		Router router = Router.router(vertx);
		router.get("/getFromCurrentPort/").handler(this::getFromCurrentPort);
		router.get("/getFromOtherPorts/").handler(this::getFromOtherPorts);
		router.get("/get/").handler(this::get);
		
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
	
	private Future<JsonObject> getFromPort(int port, String select, String where) {
	    Future<JsonObject> future = Future.future();
	    // should split string by , to get a list of select and conditions
	    Console.log("select : " +select);
	    Console.log("where : " +where);
	    
	    //String [] selects = select != null ? select.split(",") : null;
	    //String [] wheres = where != null ? where.split(",") : null;
	    
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
	
	
	private void getFromCurrentPort(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		try {

			int result_VTS = parser.getTable().getIndexes().get(0).getIndexCol().get(0).get("VTS").size();
			
			response.putHeader("Content-Type", "application/json").setChunked(true);
			response.write(new JsonObject().put("result_"+port, result_VTS).encode());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.write(e.getMessage());
			response.setStatusCode(500);
		} 
		
		response.end();
		
	}
	

	private void getFromOtherPorts(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		Future<JsonObject> getOtherPort1 = getFromPort(Integer.parseInt(otherPorts[0]), null, null);
		Future<JsonObject> getOtherPort2 = getFromPort(Integer.parseInt(otherPorts[1]), null, null);

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
		
		// get select and where from url: localhost:8081/get?select=vendor_name,trip_date&where=vendor_nam=VTS,trip_date=2019
		String select = routingContext.request().params().get("select");
		String where = routingContext.request().params().get("where");
		
		Console.log(select);
		Console.log(where);
		
		Future<JsonObject> getCurrentPort = getFromPort(port, select, where);
		Future<JsonObject> getOtherPort1 = getFromPort(Integer.parseInt(otherPorts[0]), select, where);
		Future<JsonObject> getOtherPort2 = getFromPort(Integer.parseInt(otherPorts[1]), select, where);

		// simultaneously get all data
		CompositeFuture.join(getCurrentPort, getOtherPort1, getOtherPort2).setHandler(ar -> {
		  if (ar.succeeded()) {
			  
			  JsonObject result1 = ar.result().resultAt(0);
			  
			  JsonObject combined = result1.mergeIn(ar.result().resultAt(1)).mergeIn(ar.result().resultAt(2));
			  
			  response.end(combined.encodePrettily());
		    
		  } else {
		    // At least one server failed
			  response.end("err"); 
		  }
		});
		
	}
	
}
