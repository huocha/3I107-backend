package io.vertx.main;

import java.io.IOException;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
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
	    
	    router.get("/test/").blockingHandler(event -> {
			try {
				test(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	    
		    
	    vertx
	    	.createHttpServer()
	    	.requestHandler(router::accept).listen( 8080 );
  }

  private void init() {
	  table=new Table();
	  try {
			Parser parser = new Parser(table);
			parser.parse();
			
	  } catch (Exception e) {
			Console.error(e);
	  }
		  
  }
  
  private void test(RoutingContext routingContext) throws IOException {
	  init();
	  Console.log("CMT:"+table.getIndexes().get(0).getIndexCol().get(0).getIndex().get("CMT").size());
	  //Console.log("Count: "+ table.count());
	  
	 
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