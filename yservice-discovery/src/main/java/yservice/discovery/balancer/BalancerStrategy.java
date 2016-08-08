/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery.balancer;

import yservice.discovery.ServiceRegistry;

/**
 * Strategy to select the next server to process a specific service.
 * 
 * @author David Sobreira Gouvea
 * @see Strategy Design Pattern
 */
public interface BalancerStrategy {

	/**
	 * Returns the next service to be invoked.
	 * 
	 * @param uri the URI of the REST service
	 * @return the next service to be invoked.
	 */
	ServiceRegistry next(String uri);
	
}
