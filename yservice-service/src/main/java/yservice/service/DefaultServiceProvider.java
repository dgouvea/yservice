package yservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yservice.core.ServiceRegistryDescriptor;

public class DefaultServiceProvider implements ServiceProvider {

	private final String name;
	private final String host;
	private final int port;
	private final List<Service> services = new ArrayList<>();

	public DefaultServiceProvider(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public final String getHost() {
		return host;
	}

	@Override
	public final int getPort() {
		return port;
	}

	@Override
	public final List<Service> getServices() {
		return Collections.unmodifiableList(services);
	}

	public final ServiceProvider register(Service descriptor) {
		services.add(descriptor);
		return this;
	}

	@Override
	public ServiceRegistryDescriptor getDescriptor() {
		ServiceRegistryDescriptor descriptor = new ServiceRegistryDescriptor();
		descriptor.setDomain(getDomain());
		descriptor.setName(getName());
		descriptor.setVersion(getVersion());
		return descriptor;
	}
	
}
