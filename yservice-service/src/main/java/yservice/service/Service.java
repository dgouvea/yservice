package yservice.service;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import yservice.core.Method;
import yservice.core.ServiceRegistryDescriptor;

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

	default int getThreads() {
		return 0;
	}

	default boolean isGzip() {
		return true;
	}

	default String getContentType() {
		return null;
	}

	default ServiceRegistryDescriptor getDescriptor() {
		ServiceRegistryDescriptor descriptor = new ServiceRegistryDescriptor();
		descriptor.setDomain(getServiceUrl());
		descriptor.setMethod(getMethod().name());
		descriptor.setUri(getUri());
		return descriptor;
	}
	
}
