/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.server;

import java.util.ArrayList;

import org.glassfish.jersey.uri.UriTemplate;

import yservice.core.Method;

/**
 * This class represents a service registry, it is used to register and find a REST service.
 * <p>It's a immutable class, there is a static method {@link ServiceRegistry#builder()} to use a builder pattern to create it.</p>
 * <p>Example:</p>
 * <blockquote>
 * 	{@code ServiceRegistry serviceRegistry = ServiceRegistry.builder()...build();}
 * </blockquote>
 * 
 * @author David Sobreira Gouvea
 * @see ServiceRegistryBuilder
 * @see ServiceRegistryURI
 */
public class ServiceRegistry {
	
	private final String domain;
	private final Method method;
	private final ServiceRegistryURI uri;
	
	/**
	 * Constructs a newly Service Registry.
	 * <p>All parameters are mandatory.</p>
	 * 
	 * @param domain domain, e.g.: http://localhost:8080/definity-discovery
	 * @param method HTTP method, e.g.: GET, POST...
	 * @param uri service URI, e.g.: /api/service/register
	 * @throws IllegalArgumentException if one of those parameters is null or empty
	 */
	public ServiceRegistry(String domain, Method method, String uri) throws IllegalArgumentException {
		if (domain == null || domain.isEmpty()) {
			throw new IllegalArgumentException("domain cannot be null or empty");
		}
		if (method == null) {
			throw new IllegalArgumentException("method cannot be null");
		}
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("uri cannot be null or empty");
		}
		this.domain = domain;
		this.method = method;
		this.uri = new ServiceRegistryURI(uri);
	}

	/**
	 * Returns the domain part of this {@code ServiceRegistry}.
	 * 
	 * @return the domain part of this {@code ServiceRegistry}
	 */
	public final String getDomain() {
		return domain;
	}

	/**
	 * Returns the method part of this {@code ServiceRegistry}.
	 * 
	 * @return the method part of this {@code ServiceRegistry}
	 * @see Method
	 */
	public final Method getMethod() {
		return method;
	}

	/**
	 * Returns the URI representative part of this {@code ServiceRegistry}.
	 * 
	 * @return the URI representative part of this {@code ServiceRegistry}
	 * @see ServiceRegistryURI
	 */
	public final ServiceRegistryURI getUri() {
		return uri;
	}
	
	/**
	 * Returns <code>true</code> if, and only if, this service registry URI matches the given service URI.
	 * 
	 * @param uri the URI used to call a service	
	 * @return <code>true</code> if, and only if, this service registry URI matches the given service URI 
	 * @see ServiceRegistryURI#matches(String)
	 */
	public boolean matches(String uri) {
		return this.uri.matches(uri);
	}

	/**
     * Creates an integer suitable for hash table indexing.
     * <p>The hash code is based upon all the Service Registry components relevant for comparison.<p>
     *
     * @return  a hash code for this {@code ServiceRegistry}.
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	/**
     * Compares this {@code ServiceRegistry} for equality with another object.
     * <p>Two {@code ServiceRegistry} objects are equal if they have the same domain, method
     * and URI.<p>
     * 
     * @param   obj the {@code ServiceRegistry} to compare against.
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceRegistry other = (ServiceRegistry) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	/**
     * Constructs a string representation of this {@code ServiceRegistry}. 
     *
     * @return  a string representation of this object.
     */
	@Override
	public String toString() {
		return "ServiceRegistry [domain=" + domain + ", method=" + method + ", uri=" + uri + "]";
	}

	/**
	 * Returns the new instance of {@code ServiceRegistryBuilder}.
	 * 
	 * @return the new instance of {@code ServiceRegistryBuilder}
	 * @see ServiceRegistryBuilder
	 */
	public static ServiceRegistryBuilder builder() {
		return new ServiceRegistryBuilder();
	}
	
	/**
	 * This class represents a Service Registry URI.
	 * <p>This URI is a service address representation.</p> 
	 * 
	 * @author David Sobreira Gouvea
	 */
	public static class ServiceRegistryURI implements Comparable<ServiceRegistryURI> {
		
		private final String uri;
		private final UriTemplate template;
		
		/**
		 * Constructs a newly instance of {@code ServiceRegistryURI}.
		 * 
		 * @param uri the URI of service
		 */
		public ServiceRegistryURI(String uri) {
			if (uri == null || uri.isEmpty()) {
				throw new IllegalArgumentException("uri cannot be null or empty");
			}
			
			this.uri = uri;
			this.template = new UriTemplate(uri);
		}
		
		/**
		 * Returns the URI of service.
		 * 
		 * @return the URI of service.
		 */
		public final String getUri() {
			return uri;
		}
		
		/**
		 * Returns <code>true</code> if, and only if, this service registry URI matches the given service URI.
		 * <p>It calls the {@link UriTemplate#match(CharSequence, java.util.List)} to check if matches.</p>
		 * 
		 * @param uri uri the URI used to call a service
		 * @return <code>true</code> if, and only if, this service registry URI matches the given service URI.
		 * 
		 * @see UriTemplate#match(CharSequence, java.util.List)
		 */
		public boolean matches(String uri) {
			return template.match(uri, new ArrayList<>());
		}

		/**
	     * Creates an integer suitable for hash table indexing.
	     * <p>The hash code is based in URI of this object.<p>
	     *
	     * @return  a hash code for this {@code ServiceRegistryURI}.
	     */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			return result;
		}

		/**
	     * Compares this {@code ServiceRegistryURI} for equality with another object.
	     * <p>Two {@code ServiceRegistryURI} objects are equal if they have the same URI.<p>
	     * 
	     * @param   obj the {@code ServiceRegistryURI} to compare against.
	     * @return  {@code true} if the objects are the same;
	     *          {@code false} otherwise.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServiceRegistryURI other = (ServiceRegistryURI) obj;
			if (uri == null) {
				if (other.uri != null)
					return false;
			} else if (!uri.equals(other.uri))
				return false;
			return true;
		}

		/**
	     * Constructs a string representation of the URI of {@code ServiceRegistryURI}. 
	     *
	     * @return  a string representation of the URI of this object.
	     */
		@Override
		public String toString() {
			return uri;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ServiceRegistryURI o) {
			return getUri().compareTo(o.getUri());
		}
		
	}
	
	/**
	 * This class is used to build a newly instance of {@code ServiceRegistry} class.
	 * 
	 * @author David Sobreira Gouvea
	 * @see ServiceRegistry
	 * @see Builder Design Pattern
	 */
	public static final class ServiceRegistryBuilder {
		
		private String domain;
		private Method method;
		private String uri;
	
		private ServiceRegistryBuilder() {
			
		}
		
		/**
		 * Sets the domain of the service. 
		 * 
		 * @param domain the domain of service.
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getDomain()
		 */
		public ServiceRegistryBuilder domain(String domain) {
			this.domain = domain;
			return this;
		}

		/**
		 * Sets the method of the service.
		 * 
		 * @param method the method of service.
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getMethod()
		 * @see Method#from(String)
		 */
		public ServiceRegistryBuilder method(String method) {
			return method(Method.from(method));
		}
		
		/**
		 * Sets the method of the service.
		 * 
		 * @param method the method of service.
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getMethod()
		 */
		public ServiceRegistryBuilder method(Method method) {
			this.method = method;
			return this;
		}

		/**
		 * Sets the URI of the service.
		 * 
		 * @param uri the URI of the service
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getUri()
		 * @see ServiceRegistryURI
		 */
		public ServiceRegistryBuilder uri(String uri) {
			this.uri = uri;
			return this;
		}
		
		/**
		 * Constructs a newly instance of {@code ServiceRegistry}.
		 * 
		 * @return the newly instance of {@code ServiceRegistry}
		 */
		public ServiceRegistry build() {
			return new ServiceRegistry(domain, method, uri);
		}
		
	}
	
}
