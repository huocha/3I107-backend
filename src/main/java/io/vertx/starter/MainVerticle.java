package io.vertx.starter;

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
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
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
	  
	  // initialiser les routes
	  Router router = Router.router(vertx);
	    
	    router.route().handler(BodyHandler.create());
	    // create a table (with postData in the body)
	    router.put("/table/:tableName").handler(this::createTable);
	    
	    // get a table existed, ?query=name="A"&age=21
	    router.get("/table/:tableName").handler(this::getTable);
	        
	    // #TODO: upload one file each time, not consider the multi-file 
	    router.post("/table/:tableName/importData").handler(this::handleImport);
		    
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
  
  private JsonObject loadJsonFromFile(String nameFile) throws Exception {
	  // search in "/ressource/nameFile"
	  JsonObject myReader;
	  File loadFileTable = new File(nameFile);
	  if (!loadFileTable.exists()){ log("File doesn't exist"); }
	  
	  InputStreamReader isReader;
	  
	  try {
		  isReader = new InputStreamReader(new FileInputStream(loadFileTable), "UTF-8");
		  BufferedReader rd = new BufferedReader(isReader);
		  String jsonText = readAll(rd);
					
		  myReader = new JsonObject(jsonText);
		  return myReader;
		  
	  } catch (Exception e) {
		log("error load cache from file " + e.toString());
		throw e;
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
  
  private void insert(JsonObject document) {
	  
  }
  private void handleImport(RoutingContext routingContext) {
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
		  /**
		   * #TODO: if table is existed => load the old file and update column by jsonBody
		   */
	  }
	  else {
		  /**
		   * #TODO: else create new table and save in the /ressource/tableName
		   */
		  
		  JsonArray getData;
		  String fileJson = convertCSVToJson();
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
	  // log(parsedToJson.toString());
	  // routingContext.response().end();
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
  
  private String convertCSVToJson() {
	  String pathOfJSONfile = workingDirectory + "/ressource/test.json";
	  return pathOfJSONfile;
  }
  private static void log(String string) { System.out.println(string); }
  
}