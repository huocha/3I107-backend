package io.vertx.main;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.structure.Table;
import io.vertx.utils.Console;

public class Master {
	private int slaveCount = 3;
	private int[] port = new int[]{ 8080, 8081, 8082 };
	
    private Slave[] slaves = new Slave[slaveCount];
    
    protected Vertx vertx = Vertx.vertx();
    protected Router router = Router.router(vertx);
    protected Table table;
    
    public Master(Router router) {
    	this.router = router;
    }
	
    public int slaveCount() {
    	return slaves.length;
    }
    
    public Slave getSlave(int index) {
    	return slaves[index];
    }
    
    public void addTable(Table table) {
    	this.table = table;
    }
    
    public void init() {
   	  // create slaves &
      for(int i = 0; i < slaveCount; i++) {
         slaves[i] = new Slave(port[i]);
         
         HttpServer httpServer = vertx.createHttpServer().requestHandler(this.router::accept);
         
         httpServer.listen(port[i], "localhost", res -> {
  			if (res.succeeded()) {
  				Console.log("Server is now listening on: " + httpServer.actualPort());
  			} else {
  				Console.log("Failed to bind!");
  			}
  		});
         
      }
    }
    
    public void run() {
    	
    	// start slaves:
        for(int i = 0; i < slaveCount; i++) {
           //slaves[i].start();
        }
    }
    
}
