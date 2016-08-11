package yservice.service;

import yservice.core.ServiceDiscovery;

public class HelloWorldService {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("HelloWorld", "localhost", 4004);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new HelloWorldCommand());
		
		ServiceServer.init(serviceProvider);
	}
	
}
