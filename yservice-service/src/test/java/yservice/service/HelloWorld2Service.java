package yservice.service;

import yservice.core.ServiceDiscovery;

public class HelloWorld2Service {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("hello-world", 4003);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new HelloWorld2Command());
		
		ServiceServer.init(serviceProvider);
	}
	
}
