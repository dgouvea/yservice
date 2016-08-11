package yservice.server.balancer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import yservice.discovery.ServiceManager;
import yservice.discovery.ServiceRegistry;
import yservice.discovery.balancer.RandomBalancerStrategy;

public class TestRandomBalancerStrategy {

	@Before
	public void prepare() {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		ServiceRegistry service1 = ServiceRegistry.builder().domain("http://localhost:8081").name("people").version("1.0").build();
		serviceManager.register(service1);

		ServiceRegistry service2 = ServiceRegistry.builder().domain("http://localhost:8082").name("people").version("1.0").build();
		serviceManager.register(service2);

		ServiceRegistry service3 = ServiceRegistry.builder().domain("http://localhost:8083").name("people").version("1.0").build();
		serviceManager.register(service3);

		ServiceRegistry service4 = ServiceRegistry.builder().domain("http://localhost:8084").name("people").version("1.0").build();
		serviceManager.register(service4);
	}

	@After
	@SuppressWarnings("deprecation")
	public void clean() {
		ServiceManager.getInstance().unregisterAll();
	}
	
	@Test
	public void testRound() {
		RandomBalancerStrategy strategy = new RandomBalancerStrategy();
		
		ServiceRegistry service1 = strategy.next("people", "1.0");
		ServiceRegistry service2 = strategy.next("people", "1.0");
		ServiceRegistry service3 = strategy.next("people", "1.0");
		ServiceRegistry service4 = strategy.next("people", "1.0");
		ServiceRegistry service5 = strategy.next("people", "1.0");
		ServiceRegistry service6 = strategy.next("people", "1.0");
		ServiceRegistry service7 = strategy.next("people", "1.0");
		ServiceRegistry service8 = strategy.next("people", "1.0");
		ServiceRegistry service9 = strategy.next("people", "1.0");
		ServiceRegistry service10 = strategy.next("people", "1.0");
		ServiceRegistry service11 = strategy.next("people", "1.0");

		Assert.assertFalse(service1 == service2 
				&& service1 == service3 
				&& service1 == service4 
				&& service1 == service5 
				&& service1 == service6
				&& service1 == service7
				&& service1 == service8
				&& service1 == service9
				&& service1 == service10
				&& service1 == service11);
	}
	
}
