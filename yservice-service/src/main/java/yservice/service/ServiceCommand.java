package yservice.service;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import yservice.core.Method;

public interface ServiceCommand {

	String getUri();
	
	Method getMethod();

	default boolean isGzip() {
		return true;
	}

	default String getContentType() {
		return null;
	}
	
	Object execute(Request req, Response res);
	
	default ResponseTransformer getTransformer() {
		return null;
	}

}
