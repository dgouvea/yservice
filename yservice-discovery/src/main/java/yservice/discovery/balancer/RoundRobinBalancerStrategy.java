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
import yservice.discovery.ServiceRegistry.ServiceRegistryId;

/**
 * 
 *  
 * @author David Sobreira Gouvea
 */
public class RoundRobinBalancerStrategy implements BalancerStrategy {

	private Map<ServiceRegistryId, Integer> positions = new WeakHashMap<>(); 
	
	/**
	 * Returns the next service to be invoked.
	 * 
	 * @param name the name of the REST service
	 * @param version the version of the REST service
	 * @return the next service to be invoked.
	 */
	@Override
	public ServiceRegistry next(String name, String version) {
		ServiceRegistryId serviceRegistryId = new ServiceRegistryId(name, version);
		Set<ServiceRegistry> set = ServiceManager.getInstance().get(name, version);
		
		// return the first service when there is only one
		if (set.size() == 1) {
			return set.stream().findFirst().get();
		}
		
		// transform the set to list
		List<ServiceRegistry> list = set.stream().collect(Collectors.toList());
		
		// check if this URI is already invoked
		if (!positions.containsKey(serviceRegistryId)) {
			positions.put(serviceRegistryId, -1);
		}

		// get the position from last invocation and increment it
		Integer position = positions.get(serviceRegistryId);
		if (++position >= list.size()) {
			// if position is greater than array size return position to zero
			position = 0;
		}
		
		// store the position to the cache
		positions.put(serviceRegistryId, position);
		
		// return the service to be invoked
		return list.get(position);
	}

}
