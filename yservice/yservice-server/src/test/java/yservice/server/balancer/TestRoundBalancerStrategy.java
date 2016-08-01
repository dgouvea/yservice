package yservice.server.balancer;

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRoundBalancerStrategy {

	@Before
	public void prepare() {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		ServiceRegistry service1 = ServiceRegistry.builder().domain("http://localhost:8081").method("GET").uri("/people/{name}").build();
		serviceManager.register(service1);

		ServiceRegistry service2 = ServiceRegistry.builder().domain("http://localhost:8082").method("GET").uri("/people/{name}").build();
		serviceManager.register(service2);

		ServiceRegistry service3 = ServiceRegistry.builder().domain("http://localhost:8083").method("GET").uri("/people/{name}").build();
		serviceManager.register(service3);
	}

	@Test
	public void testRound() {
		RoundRobinBalancerStrategy strategy = new RoundRobinBalancerStrategy();
		ServiceRegistry service = null;
		
		service = strategy.next("/people/david");
		Assert.assertTrue(service.getDomain().contains("8083"));

		service = strategy.next("/people/sobreira");
		Assert.assertTrue(service.getDomain().contains("8082"));

		service = strategy.next("/people/gouvea");
		Assert.assertTrue(service.getDomain().contains("8081"));

		service = strategy.next("/people/john");
		Assert.assertTrue(service.getDomain().contains("8083"));

		service = strategy.next("/people/doe");
		Assert.assertTrue(service.getDomain().contains("8082"));
	}
	
}
