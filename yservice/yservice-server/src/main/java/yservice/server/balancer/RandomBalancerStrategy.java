/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server.balancer;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;

/**
 * 
 * 
 * @author David Sobreira Gouvea
 * @see BalancerStrategy
 */
public class RandomBalancerStrategy implements BalancerStrategy {

	/**
	 * Returns the next service to be invoked.
	 * <p>Using random strategy, it choices a service in the list randomically.</p>
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
		
		// get a random service from the list
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}

}
