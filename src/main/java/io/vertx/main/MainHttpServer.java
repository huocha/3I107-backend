package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.db.Parser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class MainHttpServer extends AbstractVerticle {
	private String[] otherPorts;
	private int port;
	public JsonObject data = new JsonObject();
	public void start() throws Exception {
		otherPorts = this.context.config().getString("http.otherPorts").split(",");
		
		port = this.context.config().getInteger("http.port", 8080);
		
		Parser parser=new Parser(port);//create a parser for the actual port
		parser.parse(0); //suppose index by the column 0 -> vendor_name
		//par ex :
		int result_VTS=parser.getTable().getIndexes().get(0).getIndexCol().get(0).get("VTS").size();
		data.put("result", result_VTS);
		
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
		
		
    }
	
	private void test(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.end(data.encode());
	}
	
	private void sendData(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		response.end(data.encodePrettily());
	}

}
