package yservice.server.balancer;

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRandomBalancerStrategy {

	@Before
	public void prepare() {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		ServiceRegistry service1 = ServiceRegistry.builder().domain("http://localhost:8081").method("GET").uri("/people/{name}").build();
		serviceManager.register(service1);

		ServiceRegistry service2 = ServiceRegistry.builder().domain("http://localhost:8082").method("GET").uri("/people/{name}").build();
		serviceManager.register(service2);

		ServiceRegistry service3 = ServiceRegistry.builder().domain("http://localhost:8083").method("GET").uri("/people/{name}").build();
		serviceManager.register(service3);

		ServiceRegistry service4 = ServiceRegistry.builder().domain("http://localhost:8084").method("GET").uri("/people/{name}").build();
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
		
		ServiceRegistry service1 = strategy.next("/people/david");
		ServiceRegistry service2 = strategy.next("/people/sobreira");
		ServiceRegistry service3 = strategy.next("/people/gouvea");
		ServiceRegistry service4 = strategy.next("/people/john1");
		ServiceRegistry service5 = strategy.next("/people/doe1");
		ServiceRegistry service6 = strategy.next("/people/john2");
		ServiceRegistry service7 = strategy.next("/people/doe2");
		ServiceRegistry service8 = strategy.next("/people/john3");
		ServiceRegistry service9 = strategy.next("/people/doe3");
		ServiceRegistry service10 = strategy.next("/people/john4");
		ServiceRegistry service11 = strategy.next("/people/doe4");

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
