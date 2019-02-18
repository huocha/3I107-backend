package io.vertx.starter;

import java.util.HashMap;
import java.util.Map;

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
 * @author HuongTra
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
}