package login;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;

public class ClientVerticle extends AbstractVerticle {
	
		
	@Override
	public void start() {
		
		HttpClient client = vertx.createHttpClient();	
		//getNow(port: Int, host: String, requestURI: String, responseHandler: Handler<HttpClientResponse>)
			client.getNow(8088, "localhost", "/", new Handler<HttpClientResponse>() {

				@Override
				public void handle(HttpClientResponse event) {
					System.out.println("response");
				}
		});
	}
}
