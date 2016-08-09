package yservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

public class HelloWorldService implements DefaultService {

	private static Logger logger = LoggerFactory.getLogger(HelloWorldService.class);
	
	@Override
	public String getUri() {
		return "/hello";
	}

	@Override
	public String execute(Request req, Response res) {
		logger.warn("requesting service...");
		return "Hello World";
	}

}
