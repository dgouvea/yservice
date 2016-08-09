package yservice.service;

import java.util.List;

public interface ServiceProvider {

	String getName();
	
	String getHost();
	
	int getPort();
	
	default String getDomain() {
		return "http://" + getHost() + ":" + getPort();
	}
	
	default int getThreads() {
		return 0;
	}

	default String getVersion() {
		return "1.0.0";
	}
	
	ServiceProvider register(Service service);
	
	List<Service> getServices();
	
}
