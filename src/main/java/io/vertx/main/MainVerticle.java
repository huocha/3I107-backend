package io.vertx.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.db.Parser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.structure.Table;
import io.vertx.utils.Console;
/**
 * @author Jasmine
 * last modified: 31/03/2019
 */

public class MainVerticle extends AbstractVerticle {
	private Table table;
	private static String workingDirectory = System.getProperty("user.dir");
	private static String dataFileName = "/src/ressource/rawTest.csv"; //file source

	protected static File file = new File(workingDirectory+dataFileName);
	
	protected Master master;
	
	@Override
	public void start() throws Exception {
		VertxOptions vxOptions = new VertxOptions()
			  					.setMaxEventLoopExecuteTime(1000*60*60*15)
			  					.setMaxWorkerExecuteTime(1000*60*60*15);

		Vertx vertx = Vertx.vertx(vxOptions);
		// initialiser les routes
		Router router = Router.router(vertx);

	    // router.route().handler(BodyHandler.create());
	    // create a table (with postData in the body)
	    router.put("/table/:tableName/").handler(this::createTable);
	
	    // #TODO: insertOne data to table
	    router.post("/table/insertOne/:tableName/").handler(this::insert);
	
	    // get a table existed, ?name="A"&age=21
	    router.get("/get/").handler(this::queryTable);
	    
	    table = new Table();
	    master = new Master(router);
	    master.init();
	    
	}
	
	private void test(RoutingContext routingContext) throws IOException {
	  Parser parser = new Parser(table);
	  
	  Console.log("Count: "+ table.count());
	  int i = 0;
	  Scanner scanner = new Scanner(file);
	  
      String[] firstLine = scanner.nextLine().split(","); // firstLine is name of columns
      
      while(scanner.hasNext()) {
    	  Slave sl = master.getSlave(i++ % master.slaveCount());
    	  sl.parseLine(parser, table, scanner.nextLine());
      }
      
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
		
		if(table.count() <= 0 ) {
			response.putHeader("content-type", "plain-text").end("Nothing");
		}
		else {
			response.putHeader("content-type", "plain-text").end(table.count() + "");
		}
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
