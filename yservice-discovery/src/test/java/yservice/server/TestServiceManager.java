package yservice.server;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import yservice.discovery.ServiceManager;
import yservice.discovery.ServiceRegistry;
import yservice.discovery.ServiceRegistry.ServiceRegistryId;
import yservice.discovery.ServiceRegistryObserver;

public class TestServiceManager {

	@Test
	@SuppressWarnings("deprecation")
	public void testRegister() {
		ServiceRegistry service1a = ServiceRegistry.builder().domain("http://localhost:8080").name("people").version("1.0").build();
		ServiceRegistry service1b = ServiceRegistry.builder().domain("http://localhost:8081").name("people").version("1.0").build();
		ServiceRegistry service2 = ServiceRegistry.builder().domain("http://localhost:8082").name("companies").version("1.0").build();
		ServiceRegistry service2dup = ServiceRegistry.builder().domain("http://localhost:8082").name("companies").version("1.0").build();
		ServiceRegistry service3 = ServiceRegistry.builder().domain("http://localhost:8083").name("people").version("2.0").build();
		
		ServiceManager serviceManager = ServiceManager.getInstance();
		Assert.assertTrue(serviceManager.register(service1a));
		Assert.assertTrue(serviceManager.register(service1b));
		Assert.assertTrue(serviceManager.register(service2));
		Assert.assertFalse(serviceManager.register(service2dup)); // must be ignored
		Assert.assertTrue(serviceManager.register(service3));
		
		Assert.assertEquals(1, serviceManager.get("companies", "1.0").size());
		Assert.assertEquals(2, serviceManager.get("people", "1.0").size());
		Assert.assertEquals(1, serviceManager.get("people", "2.0").size());

		Assert.assertEquals(2, serviceManager.match("/1.0/people").size());
		Assert.assertEquals(1, serviceManager.match("/2.0/people").size());
		Assert.assertEquals(1, serviceManager.match("/1.0/companies").size());

		serviceManager.unregisterAll();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testWeakObservers() {
		ServiceRegistry service1 = ServiceRegistry.builder().domain("http://localhost:8082").name("people").version("1.0").build();
		
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		class Counter {
			int size = 0;
			boolean touch = false;
		}
		
		Counter counter = new Counter();
		
		ServiceRegistryObserver o1 = new ServiceRegistryObserver() {
			@Override
			public void update(ServiceRegistryId uri, Set<ServiceRegistry> services) {
				Assert.assertEquals(counter.size, services.size());
				counter.touch = true;
			}
		};
		
		serviceManager.addObserver(o1);
		
		// register service
		counter.size = 1;
		serviceManager.register(service1);
		
		// check if observer was called
		Assert.assertTrue(counter.touch);
		counter.touch = false;
		
		// unregister service
		counter.size = 0;
		serviceManager.unregister(service1);
		
		// check if observer was called
		Assert.assertTrue(counter.touch);
		counter.touch = false;
		
		// clean and call GC
		o1 = null;
		System.gc();
		
		// register service
		counter.size = 0;
		serviceManager.register(service1);
		
		// check if observer wasn't called
		Assert.assertFalse(counter.touch);
		
		// clean test
		serviceManager.unregisterAll();
	}
		
}
