package yservice.core;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

public interface Service {

	String getName();
	
	String getVersion();
	
	String getUrl();
	
	Method getMethod();
	
	int getPort();
	
	Object run(Request req, Response res);
	
	default ResponseTransformer getTransformer() {
		return null;
	}
	
}
