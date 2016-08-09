package yservice.service;

import spark.Request;
import spark.Response;

public class HelloService implements DefaultService {

	@Override
	public String getUri() {
		return "/hello/:name";
	}

	@Override
	public String execute(Request req, Response res) {
		return "Hi " + req.params("name");
	}

}
