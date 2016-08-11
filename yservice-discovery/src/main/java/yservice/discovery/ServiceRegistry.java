/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.discovery;

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
 * @see ServiceRegistryId
 */
public final class ServiceRegistry {
	
	private final ServiceRegistryId id;
	private final String domain;
	
	/**
	 * Constructs a newly Service Registry.
	 * <p>All parameters are mandatory.</p>
	 * 
	 * @param domain domain, e.g.: http://localhost:8080/definity-discovery
	 * @param name service name, e.g.: person
	 * @param version service version, e.g.: 1.0.0
	 * @throws IllegalArgumentException if one of those parameters is null or empty
	 */
	public ServiceRegistry(String domain, String name, String version) throws IllegalArgumentException {
		if (domain == null || domain.isEmpty()) {
			throw new IllegalArgumentException("domain cannot be null or empty");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("uri cannot be null or empty");
		}
		this.domain = domain;
		this.id = new ServiceRegistryId(name, version);
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
	 * Returns the URI representative part of this {@code ServiceRegistry}.
	 * 
	 * @return the URI representative part of this {@code ServiceRegistry}
	 * @see ServiceRegistryId
	 */
	public final ServiceRegistryId getId() {
		return id;
	}
	
	/**
	 * Returns <code>true</code> if, and only if, this service registry URI matches the given service URI.
	 * 
	 * @param uri the URI used to call a service	
	 * @return <code>true</code> if, and only if, this service registry URI matches the given service URI 
	 * @see ServiceRegistryId#matches(String)
	 */
	public boolean matches(String uri) {
		return this.id.matches(uri);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
     * Compares this {@code ServiceRegistry} for equality with another object.
     * <p>Two {@code ServiceRegistry} objects are equal if they have the same domain and ID.<p>
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "ServiceRegistry [domain=" + domain + ", uri=" + id + "]";
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
	public static final class ServiceRegistryId implements Comparable<ServiceRegistryId> {
		
		private final String name;
		private final String version;
		private final String uri;
		
		/**
		 * Constructs a newly instance of {@code ServiceRegistryURI}.
		 * 
		 * @param name the URI of service
		 */
		public ServiceRegistryId(String name, String version) {
			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("name cannot be null or empty");
			}
			if (version == null || version.isEmpty()) {
				throw new IllegalArgumentException("version cannot be null or empty");
			}
			
			this.name = name;
			this.version = version;
			this.uri = "/" + version + "/" + name;
		}
		
		/**
		 * Returns the name of service.
		 * 
		 * @return the name of service.
		 */
		public final String getName() {
			return name;
		}
		
		/**
		 * Returns the version of service.
		 * 
		 * @return the version of service
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * Returns the service URI.
		 * 
		 * @return the service URI
		 */
		public String getUri() {
			return uri;
		}
		
		/**
		 * Returns <code>true</code> if URI starts with the same version/name from service.</p>
		 * 
		 * @param uri URI the URI used to call a service
		 * @return <code>true</code> if, and only if, this service registry URI matches the given service URI.
		 * 
		 * @see UriTemplate#match(CharSequence, java.util.List)
		 */
		public boolean matches(String uri) {
			return uri.startsWith(getUri());
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
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
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
			ServiceRegistryId other = (ServiceRegistryId) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
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
			return name;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ServiceRegistryId o) {
			return getVersion().concat(getName()).compareTo(o.getVersion().concat(o.getName()));
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
		private String name;
		private String version;
	
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
		 * Sets the name of the service.
		 * 
		 * @param name the name of the service
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getId()
		 * @see ServiceRegistryId
		 */
		public ServiceRegistryBuilder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the version of the service.
		 * 
		 * @param version the version of the service
		 * @return the same instance of {@code ServiceRegistryBuilder}
		 * @see ServiceRegistry#getId()
		 * @see ServiceRegistryId
		 */
		public ServiceRegistryBuilder version(String version) {
			this.version = version;
			return this;
		}
		
		/**
		 * Constructs a newly instance of {@code ServiceRegistry}.
		 * 
		 * @return the newly instance of {@code ServiceRegistry}
		 */
		public ServiceRegistry build() {
			return new ServiceRegistry(domain, name, version);
		}
		
	}
	
}
