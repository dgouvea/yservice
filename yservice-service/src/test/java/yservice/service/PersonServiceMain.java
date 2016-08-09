package yservice.service;

import yservice.core.ServiceDiscovery;

public class PersonServiceMain extends ServiceServer {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("Person", "localhost", 4005);
		serviceProvider.register(new PersonService());
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		ServiceServer.init(discovery, serviceProvider);
	}
	
}
