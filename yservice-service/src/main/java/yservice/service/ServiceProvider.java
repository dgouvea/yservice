package yservice.service;

import java.util.List;

import yservice.core.ServiceDiscovery;
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
	
	ServiceDiscovery getServiceDiscovery();
	
	ServiceProvider serviceDiscovery(ServiceDiscovery serviceDiscovery);
	
	ServiceProvider register(ServiceCommand service);
	
	List<ServiceCommand> getCommands();

	ServiceRegistryDescriptor getDescriptor();
	
}
