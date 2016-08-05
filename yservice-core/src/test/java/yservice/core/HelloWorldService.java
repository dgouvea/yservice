package yservice.core;

import spark.Request;
import spark.Response;

public class HelloWorldService extends DefaultService {

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
		return "Hello World";
	}

}
