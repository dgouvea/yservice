package yservice.service;

import spark.Request;
import spark.Response;
import yservice.service.DefaultService;

public class HelloService extends DefaultService {

	@Override
	public String getName() {
		return "hello";
	}

	@Override
	public String getUri() {
		return "/hello/:name";
	}

	@Override
	public int getPort() {
		return 4003;
	}

	@Override
	public String run(Request req, Response res) {
		return "Hi " + req.params("name");
	}

}
