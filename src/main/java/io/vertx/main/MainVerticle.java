package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class MainVerticle extends AbstractVerticle {
	
	public void start() {
		Router router = Router.router(vertx);
		router.get("/test/").handler(this::test);	
		
		Console.log(this.context.config().getString("http.otherPorts"));
		
		vertx.createHttpServer()
			.requestHandler(router::accept)
			.listen(
	          config().getInteger("http.port", 8080),
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
		response.end("Hello world !");
	}
}
