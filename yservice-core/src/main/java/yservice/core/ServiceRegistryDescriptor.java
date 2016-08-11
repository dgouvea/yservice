/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.core;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a web service route registry.
 * 
 * @author David Sobreira Gouvea
 */
@XmlRootElement
public class ServiceRegistryDescriptor {

	private String domain;
	private String name;
	private String version;

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
	 * Returns the URI.
	 * 
	 * @return the uri
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param uri the name to set
	 */
	public void setName(String uri) {
		if (uri.contains(":")) {
			uri = uri.replaceAll("\\:(\\w+)", "{$1}");
		}
		this.name = uri;
	}

	/**
	 * Returns the version
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version
	 * 
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
