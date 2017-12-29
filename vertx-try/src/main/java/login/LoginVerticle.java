package login;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class LoginVerticle extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new LoginVerticle());
	}
	
	@Override
	public void start(Future<Void> fut) {
	 // Create a router object.
	 Router router = Router.router(vertx);

	 // Bind "/" to our hello message - so we are still compatible.
	 router.route("/").handler(routingContext -> {
		 HttpServerResponse response = routingContext.response();
	   response
	       .putHeader("content-type", "text/html")
	       .end("<h1>Hello from my first Vert.x 3 application</h1>");
	 });

	 router.route("/login").handler(routingContext -> {
		 HttpServerRequest request = routingContext.request();
		 HttpServerResponse response = routingContext.response();
		 System.out.println("로그인");
		 System.out.println(request.getParam("id"));
		   response
		       .putHeader("content-type", "text/html")
		       .end("<form id=/'login'/>"
		       		+ "<input type = /'text'/>"
		       		+ "</form>");
		 request.response().end();
	 });
	 
	 // success url
	 // Bind url test
	 router.route("/login").handler(routingContext -> {
		 HttpServerRequest request = routingContext.request();
		 HttpServerResponse response = routingContext.response();
		 System.out.println("로그인");
		   response
		       .putHeader("content-type", "text/html")
		       .end("<h1>/login</h1>");
		 request.response().end();
	 });
	 
	 // error parameter
	 // Bind login
	 router.route("/login/:_id").handler(routingContext -> {
		 HttpServerRequest request = routingContext.request();
		 System.out.println("로그인");
		 System.out.println(request.getParam("_id"));
		 request.response().end();
	 });
	 // Create the HTTP server and pass the "accept" method to the request handler.
	 vertx
	     .createHttpServer()
	     .requestHandler(router::accept)
	     .listen(
	         // Retrieve the port from the configuration,
	         // default to 8080.
	         config().getInteger("http.port", 8083),
	         result -> {
	           if (result.succeeded()) {
	             fut.complete();
	           } else {
	             fut.fail(result.cause());
	           }
	         }
	     );
	}
}