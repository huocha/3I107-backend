package io.vertx.structure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author Jasmine
 * last modified: 18/02/2019
 */

public class Demo extends AbstractVerticle {
	
private Map<String, JsonObject> products = new HashMap<>();
  @Override
  public void start() throws Exception {
	  // load data
	  setUpInitialData();
	  
	  // initialiser les routes
	  Router router = Router.router(vertx);
	    
	    router.route().handler(BodyHandler.create());
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
  /*private void handleImport(RoutingContext routingContext) {
	  BodyHandler.create().setUploadsDirectory(workingDirectory);
	  HttpServerResponse response = routingContext.response();
	  String tableName = routingContext.request().getParam("tableName");
	  
	  Set<FileUpload> listFiles = routingContext.fileUploads();
	  FileUpload file = listFiles.iterator().next();
	 
	  log("Filename: " + file.fileName() + "-" + file.uploadedFileName()+ "-" + file.name());
	  log("Size: " + file.size());
	  log("contentTransferEncoding: " + file.contentTransferEncoding());

	  String filePath = workingDirectory + "/ressource/"+tableName+".json";
	  
	  
	  File newTableFile = new File(filePath);
	  
	  // convert CSV to JSON
	  if (newTableFile.exists()) {

	  }
	  else {

		  
		  JsonArray getData;
		  String fileJson = convertCSVToJson(tableName);
		  if (fileJson != null) {
			  try {
				getData = loadJsonArrayFromFile(fileJson);
				// first object of array will be the list of column 
				formattedJson jsonObject = new formattedJson(getData.getJsonObject(0));
				JsonObject jsonWithField = jsonObject.loadKey(tableName);
				// add the data document
				JsonArray data = new JsonArray();
				for (int i = 1; i < getData.size(); i++) {
					data.add(getData.getJsonObject(i));
				}
				
				jsonWithField.put("data", data);
				
				createFile(tableName, jsonWithField);
				
				response.end();
			} catch (Exception e) {
				e.printStackTrace();
				sendError(400, response);
			}  
		  }
	  }
  }
    private JsonArray loadJsonArrayFromFile(String nameFile) throws Exception {
	// search in "/ressource/nameFile"
	  JsonArray myReader;
	  File loadFileTable = new File(nameFile);
	  if (!loadFileTable.exists()){ log("File doesn't exist"); }
	  
	  InputStreamReader isReader;
	  
	  try {
		  isReader = new InputStreamReader(new FileInputStream(loadFileTable), "UTF-8");
		  BufferedReader rd = new BufferedReader(isReader);
		  String jsonText = readAll(rd);
					
		  myReader = new JsonArray(jsonText);
		  return myReader;
		  
	  } catch (Exception e) {
		log("error load cache from file " + e.toString());
		throw e;
	  } 
  }
    
  private void getTable(RoutingContext routingContext) {
	  String tableName = routingContext.request().getParam("tableName");
	  
	  HttpServerResponse response = routingContext.response();
	  
	  String filePath = workingDirectory + "/ressource/"+tableName+".json";
	  JsonObject getData;
	  if (tableName != null) {
		  try {
			getData = loadJsonFromFile(filePath);
			response.putHeader("content-type", "application/json").end(getData.encodePrettily());
		} catch (Exception e) {
			e.printStackTrace();
			sendError(400, response);
		}  
	  }
	  
	  else { sendError(400, response); }  
	  
  }
  
  public static String readAll(Reader rd) throws IOException {
      StringBuilder sb = new StringBuilder();
      int cp;
      while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
      }
      return sb.toString();
  }
  private void createFile(String tableName, JsonObject dataBody) {
	  // create new file in the path "/ressource/tableName.json" 
	  String filePath = workingDirectory + "/ressource/"+tableName+".json";
	  log("File repo = " + filePath);
	  
	  File newTableFile = new File(filePath);
		if (!newTableFile.exists()) {
			try {
				File directory = new File(newTableFile.getParent());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				newTableFile.createNewFile();
			} catch (IOException e) {
				log("Excepton Occured: " + e.toString());
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
			log("Excepton Occured: " + e.toString());
		}
  }
  */
}