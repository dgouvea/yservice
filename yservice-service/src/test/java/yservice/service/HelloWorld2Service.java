package yservice.service;

import spark.Request;
import spark.Response;
import yservice.service.DefaultService;

public class HelloWorld2Service extends DefaultService {

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
		return 4002;
	}
	
	@Override
	public String run(Request req, Response res) {
		return "Hello World 2";
	}

}