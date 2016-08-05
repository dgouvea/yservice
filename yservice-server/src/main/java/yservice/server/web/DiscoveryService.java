/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;

/**
 * Web Service REST responsible for register/unregister the web services routes.
 * 
 * @author David Sobreira Gouvea
 */
@Path("/discovery/service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryService {

	/**
	 * Register a new web service route.
	 * 
	 * @param request the web service route registry
	 * @return the response including the service route registry.
	 */
	@PUT
	@Path("/register")
	public Response register(ServiceRegistryRequest request) {
		ServiceRegistry serviceRegistry = ServiceRegistry.builder()
				.domain(request.getDomain())
				.method(request.getMethod())
				.uri(request.getUri())
				.build();
		
		ServiceManager.getInstance().register(serviceRegistry);
		return Response.ok(request).build();
	}

	/**
	 * Unregister an existent web service route.
	 * 
	 * @param request the web service route registry
	 * @return the response including the service route registry.
	 */
	@PUT
	@Path("/unregister")
	public Response unregister(ServiceRegistryRequest request) {
		ServiceRegistry serviceRegistry = ServiceRegistry.builder()
				.domain(request.getDomain())
				.method(request.getMethod())
				.uri(request.getUri())
				.build();
		
		ServiceManager.getInstance().unregister(serviceRegistry);
		return Response.ok(request).build();
	}
	
}
