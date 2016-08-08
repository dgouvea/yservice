package yservice.server;

import org.junit.Assert;
import org.junit.Test;

import yservice.discovery.ServiceRegistry;

public class TestServiceRegistry {

	@Test
	public void testUriMatcher() {
		ServiceRegistry registry = ServiceRegistry.builder().domain("http://localhost:8080").method("GET").uri("/people/{name}").build();
		
		Assert.assertTrue(registry.matches("/people/david-sobreira-gouvea"));
		Assert.assertTrue(registry.matches("/people/david"));
		Assert.assertFalse(registry.matches("/people/"));
		Assert.assertFalse(registry.matches("/people"));
	}
	
}
