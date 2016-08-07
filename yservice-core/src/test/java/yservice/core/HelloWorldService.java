package yservice.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

public class HelloWorldService extends DefaultService {

	private static Logger logger = LoggerFactory.getLogger(HelloWorldService.class);
	
	@Override
	public String getName() {
		return "HelloWorld";
	}

	@Override
	public String getUri() {
		return "/hello";
	}

	@Override
	public int getPort() {
		return 4000;
	}
	
	@Override
	public String run(Request req, Response res) {
		logger.warn("requesting service...");
		return "Hello World";
	}

}
