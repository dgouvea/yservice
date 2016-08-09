package yservice.service;

import yservice.core.ServiceDiscovery;
import yservice.service.ServiceServer;

public class HelloWorld2ServiceMain extends ServiceServer {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("HelloWorld2", "localhost", 4003);
		serviceProvider.register(new HelloWorld2Service());
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		ServiceServer.init(discovery, serviceProvider);
	}
	
}
