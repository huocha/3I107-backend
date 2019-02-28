package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import structure.Column;
import structure.Table;
import utils.Helpers;

/**
 * @author Jasmine 
 * last modified: 28/02/2019
 */

public class Main extends AbstractVerticle {

private static String workingDirectory = System.getProperty("user.dir"); 
private Map<String, Table> tables = new HashMap<>();

  @Override
  public void start() throws Exception {
	  
	  // initialiser les routes
	  Router router = Router.router(vertx);
	    
	    router.route().handler(BodyHandler.create());
	    // create a table (with postData in the body)
	    router.put("/table/:tableName").handler(this::createTable);
	    
	    // create index (choose a column to index)
	    router.put("/table/index/:tableName").handler(this::addIndexToTable);
	    
	    // #TODO: insertOne data to table
	    router.post("/table/insertOne/:tableName/").handler(this::insert);
	    
	    // get a table existed, ?query=name="A"&age=21
	    router.get("/table/:tableName").handler(this::queryTable);
	   
		    
	    vertx
	    	.createHttpServer()
	    	.requestHandler(router::accept).listen( 8080 );
  }

 /**
  * 
  * @param routingContext
  * @data postData{
  *  fields: [
  *  	{ name: 'id', type: 'uuid' },
  *  	{ name: 'fieldX', type: 'int' }, ...
  *  ]	
  * }
  */
  
  private void createTable(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  HttpServerResponse response = routingContext.response();
	  
	  JsonObject jsonResponse = routingContext.getBodyAsJson();
	  
	  ArrayList<Column> fields = new ArrayList<>();
	  
	  JsonArray arrayFields = jsonResponse.getJsonArray("fields");
	  
	  for(int i = 0; i < arrayFields.size(); i++) {
		  JsonObject aField = arrayFields.getJsonObject(i);
		  Column f = new Column(aField.getString("name"), aField.getString("type"));
		  fields.add(f);
	  }
	  
	  Table newTable = new Table(tableName, fields);
	  tables.put(tableName, newTable);
	  
	  response.putHeader("content-type", "application/json").end();

  }
  
  private void addIndexToTable(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  HttpServerResponse response = routingContext.response();
	  
	  JsonObject jsonResponse = routingContext.getBodyAsJson();
	  
	  String indexColumn = jsonResponse.getString("newIndex");
	  
	  tables.get(tableName).addIndex(indexColumn);
	  response.end();
	  
  }
  
  private void insert(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  HttpServerResponse response = routingContext.response();
	  
	  JsonObject jsonResponse = routingContext.getBodyAsJson();
	  ArrayList<String> documents = new ArrayList<>();
	  
	  JsonArray arrayFields = jsonResponse.getJsonArray("data");

	  for(int i = 0; i < arrayFields.size(); i++) {
		  JsonObject aField = arrayFields.getJsonObject(i);	
		  documents.add(aField.getString("value"));
	  }
	 
	  Table tab = tables.get(tableName);
	  
	  tab.insertOne(documents);
	  
	  // tab.showData();
	  
	  response.end();
	  
  }
  
  private void queryTable(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  Table tab = tables.get(tableName);
	  // Map
	  // Reduce
	  String query = routingContext.request().query();
	  HttpServerResponse response = routingContext.response();
	  
	  Helpers.log(query);
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