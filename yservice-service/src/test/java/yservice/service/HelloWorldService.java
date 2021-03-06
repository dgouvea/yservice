package yservice.service;

import yservice.core.ServiceDiscovery;

public class HelloWorldService {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("hello-world", 4004);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new HelloWorldCommand());
		
		Server.init(serviceProvider);
	}
	
}
