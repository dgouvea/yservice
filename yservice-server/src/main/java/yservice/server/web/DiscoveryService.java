/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server.web;

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

import yservice.server.ServiceManager;
import yservice.server.ServiceRegistry;
import yservice.server.ServiceRegistry.ServiceRegistryURI;

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
				ServiceRegistryRequest req = new ServiceRegistryRequest();
				req.setDomain(service.getDomain());
				req.setMethod(service.getMethod().name());
				req.setUri(service.getUri().getUri());
				return req;
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
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
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
		return Response.ok(request, MediaType.APPLICATION_JSON).build();
	}
	
}
