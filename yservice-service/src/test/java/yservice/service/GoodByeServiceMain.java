package yservice.service;

import yservice.core.ServiceDiscovery;

public class GoodByeServiceMain extends ServiceServer {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("GoodBye", "localhost", 4001);
		serviceProvider.register(new GoodByeService());
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		ServiceServer.init(discovery, serviceProvider);
	}
	
}
