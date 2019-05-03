package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.utils.Console;

public class Main extends AbstractVerticle{
	
	public void start() throws Exception {
        vertx.deployVerticle(new MainVerticle());
	}
	
	
}
