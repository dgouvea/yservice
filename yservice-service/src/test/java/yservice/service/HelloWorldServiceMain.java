package yservice.service;

import yservice.core.ServiceDiscovery;

public class HelloWorldServiceMain extends ServiceServer {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("HelloWorld", "localhost", 4004);
		serviceProvider.register(new HelloWorldService());
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		ServiceServer.init(discovery, serviceProvider);
	}
	
}
