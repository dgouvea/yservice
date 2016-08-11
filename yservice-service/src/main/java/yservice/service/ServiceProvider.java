package yservice.service;

import java.util.List;

import yservice.core.ServiceRegistryDescriptor;

public interface ServiceProvider {

	String getName();
	
	String getHost();
	
	int getPort();
	
	default String getDomain() {
		StringBuilder domain = new StringBuilder();
		domain.append("http://").append(getHost()).append(":").append(Integer.toString(getPort()));
		return domain.toString();
	}
	
	default int getThreads() {
		return 0;
	}

	default String getVersion() {
		return "1.0.0";
	}
	
	ServiceProvider register(Service service);
	
	List<Service> getServices();

	ServiceRegistryDescriptor getDescriptor();
	
}
