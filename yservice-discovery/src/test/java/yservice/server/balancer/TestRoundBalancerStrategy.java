package yservice.server.balancer;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import yservice.discovery.ServiceManager;
import yservice.discovery.ServiceRegistry;
import yservice.discovery.balancer.RoundRobinBalancerStrategy;

public class TestRoundBalancerStrategy {

	@Before
	public void prepare() {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		ServiceRegistry service1 = ServiceRegistry.builder().domain("http://localhost:8081").name("people").version("1.0").build();
		serviceManager.register(service1);

		ServiceRegistry service2 = ServiceRegistry.builder().domain("http://localhost:8082").name("people").version("1.0").build();
		serviceManager.register(service2);

		ServiceRegistry service3 = ServiceRegistry.builder().domain("http://localhost:8083").name("people").version("1.0").build();
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
		
		service = strategy.next("people", "1.0");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("people", "1.0");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("people", "1.0");
		Assert.assertFalse(domains.contains(service.getDomain()));
		System.out.println(service.getDomain());
		domains.add(service.getDomain());

		service = strategy.next("people", "1.0");
		System.out.println(service.getDomain());
		Assert.assertTrue(domains.contains(service.getDomain()));
		domains.add(service.getDomain());

		service = strategy.next("people", "1.0");
		System.out.println(service.getDomain());
		Assert.assertTrue(domains.contains(service.getDomain()));
	}
	
}
