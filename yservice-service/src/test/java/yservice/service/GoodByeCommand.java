package yservice.service;

import com.mashape.unirest.http.HttpResponse;

import spark.Request;
import spark.Response;
import yservice.core.GatewayApi;
import yservice.core.Method;

public class GoodByeCommand implements DefaultServiceCommand {

	@Override
	public String getUri() {
		return "/bye";
	}

	@Override
	public String execute(Request req, Response res) {
		GatewayApi gatewayApi = GatewayApi.connect("http://localhost:8080/yservice");
		HttpResponse<String> response = gatewayApi.route("hello-world", "1.0.0").execute(Method.GET, "/hello");
		return response.getBody() + " / Good Bye";
	}

}
