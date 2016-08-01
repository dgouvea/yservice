/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server;

public class DiscoveryException extends RuntimeException {

	private static final long serialVersionUID = -55421528286309056L;

	public DiscoveryException() {

	}

	public DiscoveryException(String message) {
		super(message);
	}

	public DiscoveryException(Throwable cause) {
		super(cause);
	}

	public DiscoveryException(String message, Throwable cause) {
		super(message, cause);
	}

	protected DiscoveryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
