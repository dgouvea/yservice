/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery.web;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import yservice.core.ServiceRegistryDescriptor;
import yservice.discovery.ServiceManager;
import yservice.discovery.ServiceRegistry;
import yservice.discovery.ServiceRegistry.ServiceRegistryURI;
import yservice.discovery.balancer.BalanceStrategyManager;
import yservice.discovery.balancer.BalancerStrategy;

/**
 * Web Service REST responsible for register/unregister the web services routes.
 * 
 * @author David Sobreira Gouvea
 */
@Path("/discovery/services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryService {

	/**
	 * Return all registered services.
	 * 
	 * @param uri the service URI (optional)
	 * @return the response including the registered services.
	 */
	@GET
	public Response registeredServices(@QueryParam("uri") String uri) {
		ServiceManager serviceManager = ServiceManager.getInstance();
		Object response;
		
		if (uri == null || uri.isEmpty()) {
			Set<ServiceRegistryURI> services = serviceManager.getServices().keySet();
			response = new ServiceRegistryURIResponse(services.stream().map(serviceURI -> serviceURI.getUri()).collect(Collectors.toList()));
		} else {
			Set<ServiceRegistry> services = serviceManager.get(uri);
			response = new ServiceRegistryResponse(services.stream().map(service -> {
				ServiceRegistryDescriptor descriptor = new ServiceRegistryDescriptor();
				descriptor.setDomain(service.getDomain());
				descriptor.setMethod(service.getMethod().name());
				descriptor.setUri(service.getUri().getUri());
				return descriptor;
			}).collect(Collectors.toList()));
		}
		
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Returns the information about a service
	 * 
	 * @param domain the domain of the service
	 * @return the response including the service information.
	 */
	@GET
	@Path("/info")
	public Response serviceInfo(@QueryParam("domain") String domain) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(domain).path("/service/info");
		Response response = target.request().buildGet().invoke();
		
		return Response.ok(response.getEntity(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns the service status.
	 * 
	 * @param domain the domain of the service
	 * @return the response including the service status
	 */
	@GET
	@Path("/status")
	public Response serviceStatus(@QueryParam("domain") String domain) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(domain).path("/");
		Response response = target.request().buildGet().invoke();
		
		return Response.ok(response.getEntity(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Returns the service log.
	 * 
	 * @param domain the domain of the service
	 * @return the response including the service log
	 */
	@GET
	@Path("/log")
	public Response serviceLog(@QueryParam("domain") String domain) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(domain).path("/service/log");
		Response response = target.request().buildGet().invoke();
		
		return Response.ok(response.getEntity()).build();
	}

	/**
	 * Stop the service.
	 * 
	 * @param domain the domain of the service
	 * @return the response including the service log
	 */
	@GET
	@Path("/stop")
	public Response serviceStop(@QueryParam("domain") String domain) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(domain).path("/service/stop");
		target.request().buildDelete().invoke();
		
		return Response.ok().build();
	}
	
	/**
	 * Register a new web service route.
	 * 
	 * @param descriptor the web service route registry
	 * @return the response including the service route registry.
	 */
	@PUT
	@Path("/register")
	public Response register(ServiceRegistryDescriptor descriptor) {
		ServiceRegistry serviceRegistry = ServiceRegistry.builder()
				.domain(descriptor.getDomain())
				.method(descriptor.getMethod())
				.uri(descriptor.getUri())
				.build();
		
		ServiceManager.getInstance().register(serviceRegistry);
		return Response.ok(descriptor, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Unregister an existent web service route.
	 * 
	 * @param descriptor the web service route registry
	 * @return the response including the service route registry.
	 */
	@PUT
	@Path("/unregister")
	public Response unregister(ServiceRegistryDescriptor descriptor) {
		ServiceRegistry serviceRegistry = ServiceRegistry.builder()
				.domain(descriptor.getDomain())
				.method(descriptor.getMethod())
				.uri(descriptor.getUri())
				.build();
		
		ServiceManager.getInstance().unregister(serviceRegistry);
		return Response.ok(descriptor, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Return the service route.
	 * 
	 * @param uri the URI of the service
	 * @return the response including the service route
	 */
	@GET
	@Path("/service")
	public Response get(@QueryParam("uri") String uri) {
		BalancerStrategy balancerStrategy = BalanceStrategyManager.getInstance().getBalancerStrategy();
		ServiceRegistry serviceRegistry = balancerStrategy.next(uri);
		
		ServiceRegistryDescriptor descriptor = new ServiceRegistryDescriptor();
		descriptor.setDomain(serviceRegistry.getDomain());
		descriptor.setMethod(serviceRegistry.getMethod().name());
		descriptor.setUri(serviceRegistry.getUri().getUri());
		
		return Response.ok(descriptor).build();
	}
	
}
