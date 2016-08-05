package yservice.core;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

public interface Service {

	String getName();
	
	String getVersion();
	
	String getUri();
	
	Method getMethod();
	
	String getHost();
	
	int getPort();
	
	Object run(Request req, Response res);
	
	default String getServiceUrl() {
		return "http://" + getHost() + ":" + getPort();
	}
	
	default ResponseTransformer getTransformer() {
		return null;
	}
	
}
