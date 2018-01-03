package login;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;

public class LoginVerticle extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(LoginVerticle.class);

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
			response.putHeader("content-type", "text/html").end("<h1>Hello from my first Vert.x 3 application</h1>");
		});

		// need start point request.body, end point response.body
		router.route("/login/:id").handler(routingContext -> {
			logger.info("login start...");

			HttpServerRequest request = routingContext.request();
			logger.info(request.getParam("id"));

			// null check
			if (request.getParam("id") != null) {
				logger.info("login not null");

				// DB Connect
				JDBCClient jdbc = JDBCClient.createShared(vertx,
						new JsonObject().put("url", "jdbc:mysql://localhost/vertxtry")
								.put("driver_class", "com.mysql.jdbc.Driver").put("user", "root")
								.put("password", "root"));
				logger.info("DB Connect");

				// DB Process
				jdbc.getConnection(conn -> {
					logger.info("get connection");
					if (conn.failed()) {
						logger.info("failed make connection");
						return;
					} else {
						logger.info("make connection");
						final SQLConnection connection = (SQLConnection) conn.result();
						String sql = "select * from user where user_id = ?";
						connection.queryWithParams(sql, new JsonArray().add(request.getParam("id")), res -> {
							if (res.equals(null)) {
								logger.info("null data");
							} else {
								logger.info("not null data");
								ResultSet rsSet = res.result();
								List<JsonArray> jsonArray = rsSet.getResults();

								String id = null;
								for (JsonArray ja : jsonArray) {
									id = ja.getString(0);
									logger.info("id " + ja.getString(0));
								}
							}
						});
					}
					;
				});
			} else {
				logger.info("Invalid input id value");
				logger.info(request.getParam("id"));
			}
			request.response().end();
			logger.info("login end...");
		});

		// Create the HTTP server and pass the "accept" method to the request handler.
		vertx.createHttpServer().requestHandler(router::accept).listen(

				// Retrieve the port from the configuration,
				// default to 8080.
				config().getInteger("http.port", 8083), result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
	}

}
