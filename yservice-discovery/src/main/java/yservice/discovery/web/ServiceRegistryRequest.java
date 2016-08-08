/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery.web;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a web service route registry.
 * 
 * @author David Sobreira Gouvea
 */
@XmlRootElement
public class ServiceRegistryRequest {

	private String domain;
	private String method;
	private String uri;

	/**
	 * Returns the domain.
	 * 
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 * 
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Returns the method.
	 * 
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the method.
	 * 
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Returns the URI.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the URI.
	 * 
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		if (uri.contains(":")) {
			uri = uri.replaceAll("\\:(\\w+)", "{$1}");
		}
		this.uri = uri;
	}

}
