package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class MainVerticle extends AbstractVerticle {
	
	public void start() {
		Router router = Router.router(vertx);
		router.get("/test/").handler(this::test);	
	        
		vertx.createHttpServer().requestHandler(router::accept).listen( 8080 );
        Console.log("MainVerticle");
    }
	
	private void test(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.end("Hello world !");
	}
}
