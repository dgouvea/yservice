package yservice.core;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Route;
import spark.Spark;

public class ServiceMain {

	private static final Logger logger = LoggerFactory.getLogger(ServiceMain.class);
	
	public static void main(String[] args) {
		Service service = new TestService();
		
		Route route = (req, res) -> {
			return service.run(req, res);
		};
		
		Spark.exception(Exception.class, (e, req, res) -> {
			e.printStackTrace();
		});
		
		try {
			int port = service.getPort();
			port(port);
			logger.info("Service " + service.getName() + " starting at " + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (service.getMethod().isGet()) {
			get(service.getUrl(), route);
		} else if (service.getMethod().isPost()) {
			post(service.getUrl(), route);
		} else if (service.getMethod().isPut()) {
			put(service.getUrl(), route);
		} else if (service.getMethod().isDelete()) {
			delete(service.getUrl(), route);
		} else if (service.getMethod().isTrace()) {
			trace(service.getUrl(), route);
		} else if (service.getMethod().isHead()) {
			head(service.getUrl(), route);
		} else if (service.getMethod().isOptions()) {
			options(service.getUrl(), route);
		}
		
		System.out.println("ok");
	}
	
}
