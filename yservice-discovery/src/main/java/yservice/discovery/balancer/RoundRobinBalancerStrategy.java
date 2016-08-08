/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery.balancer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import yservice.discovery.ServiceManager;
import yservice.discovery.ServiceRegistry;
import yservice.discovery.ServiceRegistry.ServiceRegistryURI;

/**
 * 
 *  
 * @author David Sobreira Gouvea
 */
public class RoundRobinBalancerStrategy implements BalancerStrategy {

	private Map<ServiceRegistryURI, Integer> positions = new WeakHashMap<>(); 
	
	/**
	 * Returns the next service to be invoked.
	 * 
	 * @param uri the URI of the REST service
	 * @return the next service to be invoked.
	 */
	@Override
	public ServiceRegistry next(String uri) {
		Set<ServiceRegistry> set = ServiceManager.getInstance().match(uri);
		
		// return the first service when there is only one
		if (set.size() == 1) {
			return set.stream().findFirst().get();
		}
		
		// transform the set to list
		List<ServiceRegistry> list = set.stream().collect(Collectors.toList());
		
		// get the URI form the first service (because all URIs are the equals)
		ServiceRegistryURI serviceRegistryURI = list.get(0).getUri();
		
		// check if this URI is already invoked
		if (!positions.containsKey(serviceRegistryURI)) {
			positions.put(serviceRegistryURI, -1);
		}

		// get the position from last invocation and increment it
		Integer position = positions.get(serviceRegistryURI);
		if (++position >= list.size()) {
			// if position is greater than array size return position to zero
			position = 0;
		}
		
		// store the position to the cache
		positions.put(serviceRegistryURI, position);
		
		// return the service to be invoked
		return list.get(position);
	}

}
