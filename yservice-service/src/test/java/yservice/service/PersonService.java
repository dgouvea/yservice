package yservice.service;

import yservice.core.ServiceDiscovery;

public class PersonService {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("people", 4005);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new HelloWorldCommand());
		serviceProvider.register(new PersonCommand());
		
		Server.init(serviceProvider);
	}
	
}
