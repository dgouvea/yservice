/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web.processor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import yservice.gatewayapi.web.RequestWrapper;
import yservice.gatewayapi.web.ResponseWrapper;

/**
 * This process executor is responsible for logging the request and response for each service.
 * 
 * @author David Sobreira Gouvea
 * @see ProcessExecutor
 */
public class LoggerProcessExecutor extends AbstractProcessExecutor {

	private static final Logger logger = Logger.getLogger(LoggerProcessExecutor.class.getName());
	private final Level level;
	
	/**
	 * Constructs a newly instance of {@code LoggerProcessExecutor}.
	 * <p>By default the level of logging is FINE.</p>
	 */
	public LoggerProcessExecutor() {
		this(Level.FINE);
	}
	
	/**
	 * Constructs a newly instance of {@code LoggerProcessExecutor}.
	 * 
	 * @param level the level of logging
	 */
	public LoggerProcessExecutor(Level level) {
		this.level = level;
		setNext(new DefaultProcessExecutor());
	}

	/**
	 * Logging the request and response of service invocation.
	 * 
	 * @param req the request
	 * @param chain the process executor chain
	 * @return the response of service invocation
	 */
	@Override
	protected ResponseWrapper execute(RequestWrapper req, ProcessExecutorChain chain) throws IOException {
		// log request
		logger.log(level, req.toString());
		
		// execute
		ResponseWrapper res = chain.next(req);
		
		// log response
		logger.log(level, res.toString());
		
		return res;
	}

}
