package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class MainVerticle extends AbstractVerticle {
	private String[] otherPorts;
	private int port;
	public void start() {
		otherPorts = this.context.config().getString("http.otherPorts").split(",");
		port = this.context.config().getInteger("http.port", 8080);
		
		Router router = Router.router(vertx);
		router.get("/test/").handler(this::test);
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
		
		HttpClient client = vertx.createHttpClient();
		
		RequestOptions options = new RequestOptions();
		options.setPort(Integer.parseInt(otherPorts[0]));
		options.setURI("/postData");
	
		client.post(options, res -> {

			  Console.log("Received response with status code " + res.statusCode());
			  res.bodyHandler(bodyHandler -> {
				  JsonObject body = bodyHandler.toJsonObject();
				  Console.log(body.toString());
			  });
		}).end();
    }
	
	private void test(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.end("Hello world !");
	}
	
	private void sendData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		JsonObject toReturn = new JsonObject();
		
		toReturn.put("to", port);
		toReturn.put("from", otherPorts[0]);
		response.end(toReturn.encodePrettily());
	}

}
