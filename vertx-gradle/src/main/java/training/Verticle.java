package training;

import io.vertx.core.AbstractVerticle;

public class Verticle extends AbstractVerticle {
	@Override
	public void start() {
		System.out.println("localhost:8084 브라우저 확인");
		vertx.createHttpServer().requestHandler(req -> req.response().end("Welcome Vert.x")).listen(8084);
	}
}
