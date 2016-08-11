package yservice.service;

import spark.Request;
import spark.Response;

public class HelloCommand implements DefaultServiceCommand {

	@Override
	public String getUri() {
		return "/hello/:name";
	}

	@Override
	public String execute(Request req, Response res) {
		return "Hi " + req.params("name");
	}

}
