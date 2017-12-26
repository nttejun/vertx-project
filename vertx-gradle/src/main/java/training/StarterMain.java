package training;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class StarterMain extends AbstractVerticle {
	public static void main(String[] args) throws Exception {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Verticle());
	}
}
