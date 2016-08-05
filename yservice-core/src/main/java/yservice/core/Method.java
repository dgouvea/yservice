/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.core;

/**
 * This enum represents a HTTP Method.
 * 
 * @author David Sobreira Gouvea
 */
public enum Method {

	/**
	 * GET
	 */
	GET,
	
	/**
	 * POST
	 */
	POST,
	
	/**
	 * PUT
	 */
	PUT,
	
	/**
	 * DELETE
	 */
	DELETE,
	
	/**
	 * TRACE
	 */
	TRACE,
	
	/**
	 * HEAD
	 */
	HEAD,
	
	/**
	 * OPTIONS
	 */
	OPTIONS;
	
	/**
	 * Returns <code>true</code> if this method allows request body, or <code>false</code> otherwise.
	 * <p>The only methods that body is allowed are: POST and PUT.</p>
	 * 
	 * @return <code>true</code> if this method allows request body, or <code>false</code> otherwise.
	 */
	public boolean isBodyAllowed() {
		return this == POST || this == PUT;
	}
	
	/**
	 * Returns <code>true</code> if is a GET method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a GET method or <code>false</code> otherwise.
	 */
	public boolean isGet() {
		return this == GET;
	}

	/**
	 * Returns <code>true</code> if is a POST method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a POST method or <code>false</code> otherwise.
	 */
	public boolean isPost() {
		return this == POST;
	}

	/**
	 * Returns <code>true</code> if is a PUT method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a PUT method or <code>false</code> otherwise.
	 */
	public boolean isPut() {
		return this == PUT;
	}

	/**
	 * Returns <code>true</code> if is a DELETE method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a DELETE method or <code>false</code> otherwise.
	 */
	public boolean isDelete() {
		return this == DELETE;
	}

	/**
	 * Returns <code>true</code> if is a TRACE method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a TRACE method or <code>false</code> otherwise.
	 */
	public boolean isTrace() {
		return this == TRACE;
	}

	/**
	 * Returns <code>true</code> if is a HEAD method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a HEAD method or <code>false</code> otherwise.
	 */
	public boolean isHead() {
		return this == HEAD;
	}

	/**
	 * Returns <code>true</code> if is a OPTIONS method or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if is a OPTIONS method or <code>false</code> otherwise.
	 */
	public boolean isOptions() {
		return this == OPTIONS;
	}
	
	/**
	 * Returns a {@code Method} representative.
	 * 
	 * @param value the string of HTTP Method
	 * @return {@code Method} representative
	 * @throws IllegalArgumentException if method was not found
	 */
	public static Method from(String value) throws IllegalArgumentException {
		for (Method method : values()) {
			if (method.name().equalsIgnoreCase(value)) {
				return method;
			}
		}
		throw new IllegalArgumentException(value + " is not a valid HTTP method");
	}
	
}
