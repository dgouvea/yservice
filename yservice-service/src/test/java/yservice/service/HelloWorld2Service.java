package yservice.service;

import spark.Request;
import spark.Response;

public class HelloWorld2Service implements DefaultService {

	@Override
	public String getUri() {
		return "/hello";
	}

	@Override
	public String execute(Request req, Response res) {
		return "Hello World 2";
	}

}
