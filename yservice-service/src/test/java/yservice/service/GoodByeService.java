package yservice.service;

import com.mashape.unirest.http.HttpResponse;

import spark.Request;
import spark.Response;
import yservice.core.Method;
import yservice.service.DefaultService;
import yservice.service.ServiceDiscovery;

public class GoodByeService extends DefaultService {

	@Override
	public String getName() {
		return "GoodBye";
	}

	@Override
	public String getUri() {
		return "/bye";
	}

	@Override
	public int getPort() {
		return 4001;
	}
	
	@Override
	public String run(Request req, Response res) {
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		HttpResponse<String> response = discovery.route(Method.GET, "/hello");
		return response.getBody() + " / Good Bye";
	}

}
