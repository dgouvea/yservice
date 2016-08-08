/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.stream.Collectors;

import yservice.discovery.ServiceRegistry.ServiceRegistryURI;

/**
 * This class is used to manage all services registration.
 * 
 * @author David Sobreira Gouvea
 */
public final class ServiceManager {

	private static volatile ServiceManager instance;
	
	private final Map<ServiceRegistryURI, Set<ServiceRegistry>> services = new HashMap<>();
	private final List<WeakReference<ServiceRegistryObserver>> observers = new ArrayList<>();
	
	private ServiceManager() {
		
	}
	
	/**
	 * Returns a single instance of {@code ServiceManager} class.
	 * 
	 * @return a single instance of {@code ServiceManager} class
	 */
	public static ServiceManager getInstance() {
		if (instance == null) {
			synchronized (ServiceManager.class) {
				instance = new ServiceManager();
			}
		}
		return instance;
	}
	
	/**
	 * Returns the list of routes.
	 * 
	 * @return the list of routes
	 */
	public Map<ServiceRegistryURI, Set<ServiceRegistry>> getServices() {
		return Collections.unmodifiableMap(services);
	}
	
	/**
	 * Adds an observer to the list of observers.
	 * <p>The observers are stored as {@link WeakReference}, so if the observer was collected by Garbage Collector, it will be removed to the list of observers.</p>
	 * 
	 * @param observer the observer to be added
	 * @see ServiceRegistryObserver
	 * @see ServiceManager#notifyObservers(ServiceRegistryURI, Set)
	 * @see WeakReference
	 * @see Observer Design Pattern
	 */
	public void addObserver(ServiceRegistryObserver observer) {
		this.observers.add(new WeakReference<ServiceRegistryObserver>(observer));
	}

	/**
	 * Removes an observer from the list of observers.
	 * <p>The observers are stored as {@link WeakReference}, so if the observer was collected by Garbage Collector, it will be removed to the list of observers.</p>
	 * 
	 * @param observer the observer to be removed
	 * @see ServiceRegistryObserver
	 * @see ServiceManager#notifyObservers(ServiceRegistryURI, Set)
	 * @see WeakReference
	 * @see Observer Design Pattern
	 */
	public void removeObserver(ServiceRegistryObserver observer) {
		this.observers.removeIf(o -> o.get().equals(observer));
	}
	
	/**
	 * Register a new service in the {@code ServiceManager}.
	 * <p>Returns <code>true</code> if the service was registered or <code>false</code> otherwise.</p>
	 * <p>After service is registered, all observers will be notified.</p>
	 * 
	 * @param service the service to be registered
	 * @return <code>true</code> if service was registered, <code>false</code> otherwise
	 * @see ServiceRegistryObserver
	 * @see ServiceManager#notifyObservers(ServiceRegistryURI, Set)
	 */
	public boolean register(ServiceRegistry service) {
		Set<ServiceRegistry> list;
		
		// get or create a list of services from the services map
		if (services.containsKey(service.getUri())) {
			list = services.get(service.getUri());
		} else {
			list = new HashSet<>();
			services.put(service.getUri(), list);
		}
		
		// register the service
		boolean added = list.add(service);
		
		// if service was registered, notify all observers
		if (added) {
			notifyObservers(service.getUri(), list);
		}
		
		return added;
	}

	/**
	 * Unregister a service from the {@code ServiceManager}.
	 * <p>Returns <code>true</code> if the service was unregistered or <code>false</code> otherwise.</p>
	 * <p>After service is unregistered, all observers will be notified.</p>
	 * 
	 * @param service the service to be unregistered
	 * @return <code>true</code> if the service was unregistered or <code>false</code> otherwise
	 * @see ServiceRegistryObserver
	 * @see ServiceManager#notifyObservers(ServiceRegistryURI, Set)
	 */
	public boolean unregister(ServiceRegistry service) {
		// check if the service URI is on map
		if (!services.containsKey(service.getUri())) {
			return false;
		}

		// get a list of services from a specific URI 
		Set<ServiceRegistry> list = services.get(service.getUri());
		
		// remove the service from the list
		boolean removed = list.remove(service);
		
		// if service was removed, notify all observers
		if (removed) {
			notifyObservers(service.getUri(), list);
		}
		
		// if there are no services in the list, remove the service from the map
		if (list.isEmpty()) {
			services.remove(service.getUri());
		}
		
		return removed;
	}
	
	@Deprecated
	public void unregisterAll() {
		services.clear();
	}
	
	/**
	 * Returns a {@code Set} of services related to the given URI.
	 * 
	 * @param uri the service URI
	 * @return a {@code Set} of services related to the given URI
	 * @throws DiscoveryException if URI was not registered
	 */
	public Set<ServiceRegistry> get(String uri) throws DiscoveryException {
		ServiceRegistryURI serviceRegistryURI = new ServiceRegistryURI(uri);

		if (!services.containsKey(serviceRegistryURI)) {
			throw new DiscoveryException("No services registered to URI " + uri);
		}
		
		Set<ServiceRegistry> list = services.get(serviceRegistryURI);
		return Collections.unmodifiableSet(list);
	}
	
	/**
	 * Return a list of services from the first {@code ServiceRegistryURI} which matches the given URI.
	 * <p>The method {@link ServiceRegistryURI#matches(String)} will be called for each service registered.</p>
	 * 
	 * @param uri the service URI
	 * @return all services which matches the given URI
	 * @see ServiceRegistryURI#matches(String)
	 */
	public Set<ServiceRegistry> match(String uri) {
		return services.keySet().stream().filter(sru -> sru.matches(uri)).findFirst().map(sru -> services.get(sru)).get();
	}

	/**
	 * Returns a {@code Set} with all services which matches with a URI.
	 * <p>The method {@link ServiceRegistryURI#matches(String)} will be called for each service registered.</p>
	 *  
	 * @param uri the service URI
	 * @return a {@code Set} with all services which matches with a URI
	 * @see ServiceRegistryURI#matches(String)
	 */
	public Set<ServiceRegistry> allMatches(String uri) {
		return services.keySet().stream().filter(sru -> sru.matches(uri)).map(sru -> services.get(sru)).flatMap(Set::stream).collect(Collectors.toSet());
	}

	/**
	 * Notify all observers the service changes for an specific URI.
	 * <p>After notify remove all observers which theirs instances was collected by Garbage Collector.</p>
	 * 
	 * @param uri the URI of service.
	 * @param services list of service registers
	 * @see ServiceRegistryObserver
	 * @see ServiceManager#addObserver(ServiceRegistryObserver)
	 * @see WeakReference
	 * @see Observer Design Pattern
	 */
	private void notifyObservers(ServiceRegistryURI uri, Set<ServiceRegistry> services) {
		List<WeakReference<ServiceRegistryObserver>> setToRemove = new ArrayList<>();
		
		// notify all observers
		observers.forEach(o -> {
			ServiceRegistryObserver observer = o.get();
			if (observer != null) {
				// notify observer
				observer.update(uri, Collections.unmodifiableSet(services));
			} else {
				// mark to remove observer
				setToRemove.add(o);
			}
		});
		
		// remove all weak references
		if (!setToRemove.isEmpty()) {
			observers.removeAll(setToRemove);
		}
	}
	
}
