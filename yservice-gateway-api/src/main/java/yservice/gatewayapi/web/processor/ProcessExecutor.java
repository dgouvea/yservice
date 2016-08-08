/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web.processor;

import java.io.IOException;

import yservice.gatewayapi.web.RequestWrapper;
import yservice.gatewayapi.web.ResponseWrapper;

/**
 * A class can implement the {@code ProcessExecutor} interface when it
 * wants to handle or to process a request to a REST service.
 * 
 * @author David Sobreira Gouvea
 * @see Chain of Responsibility Design Pattern
 */
public interface ProcessExecutor {

	/**
	 * Define the next processor in the chain to execute the request.
	 * 
	 * @param processExecutor the next processor to execute the request
	 * @return the next processor to execute the request, the processExecutor parameter
	 * @see Chain of Responsibility Design Pattern
	 */
	ProcessExecutor setNext(ProcessExecutor processExecutor);
	
	/**
	 * Processes the request.
	 * 
	 * @param req the request representation
	 * @return the response representation
	 * @throws IOException if an input or output error occurs while the HTTP request was being handled 
	 */
	ResponseWrapper execute(RequestWrapper req) throws IOException;

}
