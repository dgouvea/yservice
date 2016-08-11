package yservice.server;

import org.junit.Assert;
import org.junit.Test;

import yservice.discovery.ServiceRegistry;

public class TestServiceRegistry {

	@Test
	public void testUriMatcher() {
		ServiceRegistry registry = ServiceRegistry.builder().domain("http://localhost:8080").name("people").version("1.0").build();
		
		Assert.assertTrue(registry.matches("/1.0/people/david-sobreira-gouvea"));
		Assert.assertTrue(registry.matches("/1.0/people/david"));
		Assert.assertFalse(registry.matches("/2.0/people/"));
		Assert.assertFalse(registry.matches("/1.0/person/"));
		Assert.assertTrue(registry.matches("/1.0/people"));
	}
	
}
