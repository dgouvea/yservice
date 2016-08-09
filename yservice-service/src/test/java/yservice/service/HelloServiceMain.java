package yservice.service;

import yservice.core.ServiceDiscovery;
import yservice.service.ServiceServer;

public class HelloServiceMain {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("Hello", "localhost", 4002);
		serviceProvider.register(new HelloService());
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		ServiceServer.init(discovery, serviceProvider);
	}
	
}
