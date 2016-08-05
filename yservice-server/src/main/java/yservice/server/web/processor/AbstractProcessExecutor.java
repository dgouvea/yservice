/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server.web.processor;

import java.io.IOException;

import yservice.server.web.RequestWrapper;
import yservice.server.web.ResponseWrapper;

/**
 * Abstract class responsible to help process executor to implement the chain of responsibility pattern.
 * 
 * @author David Sobreira Gouvea
 * @see ProcessExecutor
 * @see Chain of Responsibility Design Pattern
 */
public abstract class AbstractProcessExecutor implements ProcessExecutor {

	private ProcessExecutor next;
	
	/**
	 * Sets the next process executor.
	 * 
	 * @param next the next process executor
	 * @return the next process executor
	 */
	@Override
	public ProcessExecutor setNext(ProcessExecutor next) {
		return this.next = next;
	}
	
	/**
	 * Processes the request.
	 * <p>Delegate to {@link AbstractProcessExecutor#execute(RequestWrapper, ProcessExecutorChain)}.</p>
	 * 
	 * @param req the request representation
	 * @return the response representation
	 * @throws IOException if an input or output error occurs while the HTTP request was being handled
	 * @see AbstractProcessExecutor#execute(RequestWrapper, ProcessExecutorChain) 
	 */
	@Override
	public final ResponseWrapper execute(RequestWrapper req) throws IOException {
		return execute(req, new ProcessExecutorChain());
	}
	
	/**
	 * Process the request.
	 * 
	 * @param req the request
	 * @param chain the process executor chain
	 * @return the response
	 * @throws IOException if an input or output error occurs while the HTTP request was being handled
	 */
	protected abstract ResponseWrapper execute(RequestWrapper req, ProcessExecutorChain chain) throws IOException;
	
	/**
	 * Encapsulate the next process executor invocation.
	 * 
	 * @author David Sobreira Gouvea
	 */
	public final class ProcessExecutorChain {
		
		/**
		 * Invoke the next process executor in the chain.
		 * <p>If there is not a next process executor, it will returns null.</p>
		 * 
		 * @param req the request
		 * @return the response
		 * @throws IOException if an input or output error occurs while the HTTP request was being handled
		 */
		public ResponseWrapper next(RequestWrapper req) throws IOException {
			if (next != null) {
				return next.execute(req);
			}
			return null;
		}
		
	}
	
}
