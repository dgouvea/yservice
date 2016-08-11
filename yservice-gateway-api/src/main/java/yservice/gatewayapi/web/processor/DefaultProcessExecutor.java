/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web.processor;

import java.io.IOException;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import yservice.core.ServiceDiscovery;
import yservice.core.ServiceRegistryDescriptor;
import yservice.gatewayapi.web.RequestWrapper;
import yservice.gatewayapi.web.ResponseWrapper;

/**
 * This class is responsible to invoke a REST web service and return its response.
 * 
 * @author David Sobreira Gouvea
 */
public class DefaultProcessExecutor extends AbstractProcessExecutor {

	private final ServiceDiscovery discovery;
	
	public DefaultProcessExecutor(ServiceDiscovery discovery) {
		this.discovery = discovery;
	}

	/**
	 * It throws an IllegalArgumentException, 
	 * next is not allowed to the DefaultProcessExecutor.
	 * <p>Please, do not call this method.</p>
	 * 
	 * @param next the next processor to execute the request
	 * @throw IllegalArgumentException always
	 * @deprecated next is not allowed to the DefaultProcessExecutor
	 */
	@Override
	@Deprecated
	public ProcessExecutor setNext(ProcessExecutor next) {
		throw new IllegalArgumentException("next is not allowed to the DefaultProcessExecutor");
	}
	
	/**
	 * Invoke the REST service.
	 * <p>Returns the response from REST service invocation.</p>
	 * 
	 * @param req the request
	 * @param chain the chain object to invoke the next in chain
	 * @return the response from service invocation
	 * @throws IOException 
	 * @see {@link ResponseWrapper}
	 */
	@Override
	protected ResponseWrapper execute(RequestWrapper req, ProcessExecutorChain chain) throws IOException {
		String uri = req.getUri();
		String[] uriParts = uri.split("/");
		String version = uriParts[0];
		String name = uriParts[1];
		ServiceRegistryDescriptor descriptor = discovery.service(name, version);
		
		// rest client
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(descriptor.getDomain()).path(descriptor.getName());
		
		// query parameters (cannot use forEach because queryParam return a new instance of target)
		for (Entry<String, String> entry : req.getParameters().entrySet()) {
			target = target.queryParam(entry.getKey(), entry.getValue());
		}
		
		// request
		Builder request = target.request();

		req.getCookies().forEach(request::cookie);
		req.getHeaders().forEach(request::header);
		
		// invoke service
		Response response;
		
		if (req.hasBody()) {
			Entity<String> entity = Entity.entity(req.getBody(), req.getContentType());
			response = request.method(req.getMethod().name(), entity);
		} else {
			response = request.method(req.getMethod().name());
		}
		
		return ResponseWrapper.builder().response(response).build();
	}
	
}
