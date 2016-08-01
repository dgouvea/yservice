/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server;

import java.util.Set;

import yservice.server.ServiceRegistry.ServiceRegistryURI;

/**
 * A class can implement the {@code ServiceRegistryObserver} interface when it
 * wants to be informed of changes in {@code ServiceManager}.
 * 
 * @author David Sobreira Gouvea
 * @see ServiceManager
 * @see Observer Design Pattern
 */
@FunctionalInterface
public interface ServiceRegistryObserver {

	/**
	 * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
	 * 
	 * @param uri
	 * @param services
	 */
	void update(ServiceRegistryURI uri, Set<ServiceRegistry> services);
	
}
