package yservice.core;

import spark.Request;
import spark.Response;

public class TestService extends DefaultService {

	@Override
	public String getName() {
		return "Test";
	}

	@Override
	public String getUrl() {
		return "/test";
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
