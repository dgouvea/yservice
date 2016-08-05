package yservice.core;

import spark.Request;
import spark.Response;

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
		return "Good Bye";
	}

}
