package io.vertx.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class Client extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    client.get(80, "nicolas.club1.fr", "/2017/").send(ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        System.out.println("Got HTTP response with status " + response.statusCode() + " with data " +
          response.body().toString("ISO-8859-1"));
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}