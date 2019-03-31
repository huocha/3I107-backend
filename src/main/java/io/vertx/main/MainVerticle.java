package io.vertx.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.db.Parser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.operation.MapReduce;
import io.vertx.structure.Column;
import io.vertx.structure.Table;
import io.vertx.utils.Console;
/**
 * @author Jasmine 
 * last modified: 28/02/2019
 */

public class MainVerticle extends AbstractVerticle {
	private Table table = new Table();
	
	@Override
	public void start() throws Exception {
		VertxOptions vxOptions = new VertxOptions()
			  					.setMaxEventLoopExecuteTime(1000*60*60*15)
			  					.setMaxWorkerExecuteTime(1000*60*60*15); 

		Vertx vertx = Vertx.vertx(vxOptions);
		// initialiser les routes
		Router router = Router.router(vertx);
	    
	    router.route().handler(BodyHandler.create());
	    // create a table (with postData in the body)
	    router.put("/table/:tableName/").handler(this::createTable);
	    
	    // #TODO: insertOne data to table
	    router.post("/table/insertOne/:tableName/").handler(this::insert);
	    
	    // get a table existed, ?name="A"&age=21
	    router.get("/table/:tableName/").handler(this::queryTable);
	    
	    router.get("/test/").blockingHandler(this::test);
	    
		    
	    vertx
	    	.createHttpServer()
	    	.requestHandler(router::accept).listen( 8080 );
  }

  private void init() {
	  try {
			Parser parser = new Parser(table);
			parser.parse();
			
	  } catch (Exception e) {
			Console.error(e);
	  }
		  
  }
  
  private void test(RoutingContext routingContext) {
	  init();
	  Console.log("Count: "+ table.count());
	 
	  // Console.log(table.getColumns().toString());
	 
	  String query = routingContext.request().query();
	 
	  // Console.log(query);
	 
	  routingContext.response()
      .putHeader("content-type", "text/plain")
      .end("Hello from Vert.x!");
  }
  
  private void createTable(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  HttpServerResponse response = routingContext.response();
	  
	  response.putHeader("content-type", "application/json").end();

  }
  
  
  private void insert(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  HttpServerResponse response = routingContext.response();
	  
	  
	  response.end();
	  
  }
  
  private void queryTable(RoutingContext routingContext) {
	  
	  HttpServerResponse response = routingContext.response();
	  
	  response.end();
  }
  
  // HELPERS
  /**
   * 
   * @param statusCode
   * @param response
   */
  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }
 
 
}