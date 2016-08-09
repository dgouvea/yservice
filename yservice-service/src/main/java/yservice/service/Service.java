package yservice.service;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import yservice.core.Method;
import yservice.core.ServiceRegistryDescriptor;

public interface Service {

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

	default ServiceRegistryDescriptor getDescriptor() {
		ServiceRegistryDescriptor descriptor = new ServiceRegistryDescriptor();
		descriptor.setMethod(getMethod().name());
		descriptor.setUri(getUri());
		return descriptor;
	}
	
}
