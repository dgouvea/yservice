package yservice.service;

import yservice.core.ServiceDiscovery;
import yservice.service.ServiceServer;

public class HelloService {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("Hello", 4002);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new HelloCommand());
		
		ServiceServer.init(serviceProvider);
	}
	
}
