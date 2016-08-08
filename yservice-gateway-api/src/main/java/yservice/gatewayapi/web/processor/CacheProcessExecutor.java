/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yservice.gatewayapi.web.RequestWrapper;
import yservice.gatewayapi.web.ResponseWrapper;

/**
 * This process executor is responsible for caching GET invocations and
 * return the cached response without call the service again.
 * 
 * @author David Sobreira Gouvea
 */
public class CacheProcessExecutor extends AbstractProcessExecutor {

	private final Map<String, ResponseWrapper> cache = new HashMap<>();
	
	/**
	 * Constructs a newly instance of {@code CacheProcessExecutor}.
	 * <p>By default, define the next executor in chain as {@code DefaultProcessExecutor}.</p>
	 * 
	 * @see DefaultProcessExecutor
	 */
	public CacheProcessExecutor() {
		setNext(new DefaultProcessExecutor(null)); //TODO: fix it
	}
	
	/**
	 * Process request only if response is not cached.
	 * <p>Returns the response from service invocation.</p>
	 * <p>If method is GET, the response is stored on cache, and in the next invocation, 
	 * this cache will be returned without call the service again.</p>
	 * 
	 * @param req the request
	 * @param chain the process executor chain
	 * @return the response
	 */
	@Override
	protected ResponseWrapper execute(RequestWrapper req, ProcessExecutorChain chain) throws IOException {
		String url = req.getUri() + req.getQueryString();
		
		// check if URL is on cache
		if (cache.containsKey(url)) {
			// return the cached response
			return cache.get(url);
		}
		
		// invoke the service
		ResponseWrapper response = chain.next(req);
		
		// store response on cache if method is GET
		if (req.getMethod().isGet()) {
			cache.put(url, response);
		}
		
		return response;
	}
	
}
