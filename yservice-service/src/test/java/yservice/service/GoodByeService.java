package yservice.service;

import yservice.core.ServiceDiscovery;

public class GoodByeService {

	public static void main(String[] args) {
		ServiceProvider serviceProvider = new DefaultServiceProvider("GoodBye", 4001);
		serviceProvider.serviceDiscovery(ServiceDiscovery.connect("http://localhost:8080/yservice"));
		serviceProvider.register(new GoodByeCommand());
		
		Server.init(serviceProvider);
	}
	
}
