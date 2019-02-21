package io.vertx.starter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author Jasmine 
 * last modified: 21/02/2019
 */

public class MainVerticle extends AbstractVerticle {
	
private Map<String, JsonObject> products = new HashMap<>();
private static String workingDirectory = System.getProperty("user.dir"); 

  @Override
  public void start() throws Exception {
	  // load data
	  setUpInitialData();
	  
	  // initialiser les routes
	  Router router = Router.router(vertx);
	    
	    router.route().handler(BodyHandler.create());
	    // create a table (with postData in the body)
	    router.put("/table/:tableName").handler(this::createTable);
	    
	    // get a table existed, ?query=name="A"&age=21
	    router.get("/table/:tableName").handler(this::handleGetProduct);
	    
	    router.get("/products/:productID").handler(this::handleGetProduct);
	    router.put("/products/:productID").handler(this::handleAddProduct);
	    router.get("/products").handler(this::handleListProducts);
	    
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
	  
	  JsonObject jsonResponse = routingContext.getBodyAsJson();
	  
	  // String uniqueID = UUID.randomUUID().toString();
	  
	  formattedJson js = new formattedJson(jsonResponse);
	  
	  JsonObject dataBody = js.addIdToObject("field", tableName);

	  
	  HttpServerResponse response = routingContext.response();

	  
	  // if table not exist => create one, otherwise return error
	  if (tableName != null) {
		  createFile(tableName, dataBody);
	  }
	  
	  // #TODO: need to return a success or fail message
	  response.end();

  }
  
  
  private void createFile(String tableName, JsonObject dataBody) {
	  // create new file in the path "/ressource/tableName.json" 
	  String filePath = workingDirectory + "/ressource/"+tableName+".json";
	  System.out.println("File repo = " + filePath);
	  
	  File newTableFile = new File(filePath);
		if (!newTableFile.exists()) {
			try {
				File directory = new File(newTableFile.getParent());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				newTableFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Excepton Occured: " + e.toString());
			}
		}

		try {
			// Convenience class for writing character files
			FileWriter fileTable;
			fileTable = new FileWriter(newTableFile.getAbsoluteFile(), true);

			// Writes text to a character-output stream
			BufferedWriter bufferWriter = new BufferedWriter(fileTable);
			bufferWriter.write(dataBody.toString());
			bufferWriter.close();

			
		} catch (IOException e) {
			System.out.println("Excepton Occured: " + e.toString());
		}
  }
  

  private void loadFile(String nameFile) {
	  // search in "/ressource/nameFile"
	  
  }
  
  /**
   * 
   * @param routingContext
   * GET all 
   */
  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = products.get(productID);
      if (product == null) {
        sendError(404, response);
      } else {
        response.putHeader("content-type", "application/json").end(product.encodePrettily());
      }
    }
  }
  
  /**
   * 
   * @param routingContext
   * PUT 
   */
  
  private void handleAddProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = routingContext.getBodyAsJson();
      if (product == null) {
        sendError(400, response);
      } else {
        products.put(productID, product);
        response.end();
      }
    }
  }
  
  /**
   * 
   * @param routingContext
   * GET
   */
  private void handleListProducts(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    products.forEach((k, v) -> arr.add(v));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }
  /**
   * 
   * @param statusCode
   * @param response
   */
  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }
  
  /**
   * 
   */
  
  private void setUpInitialData() {
    addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
    addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
    addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
  }

  private void addProduct(JsonObject product) {
    products.put(product.getString("id"), product);
  }
}