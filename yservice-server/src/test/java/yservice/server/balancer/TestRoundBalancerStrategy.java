package yservice.server.balancer;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;

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
	
	@After
	@SuppressWarnings("deprecation")
	public void clean() {
		ServiceManager.getInstance().unregisterAll();
	}

	@Test
	public void testRound() {
		RoundRobinBalancerStrategy strategy = new RoundRobinBalancerStrategy();
		ServiceRegistry service = null;
		
		List<String> domains = new ArrayList<>();
		
		service = strategy.next("/people/david");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("/people/sobreira");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("/people/gouvea");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("/people/john");
		System.out.println(service.getDomain());
		Assert.assertTrue(domains.contains(service.getDomain()));
		domains.add(service.getDomain());

		service = strategy.next("/people/doe");
		System.out.println(service.getDomain());
		Assert.assertTrue(domains.contains(service.getDomain()));
	}
	
}
