package yservice.service;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yservice.core.ServiceDiscovery;
import yservice.core.ServiceRegistryDescriptor;

public class DefaultServiceProvider implements ServiceProvider {

	private final String name;
	private final String host;
	private final int port;
	private ServiceDiscovery serviceDiscovery;
	private final List<ServiceCommand> commands = new ArrayList<>();

	public DefaultServiceProvider(String name, int port) {
		this.name = name;
		this.port = port;
		
		try {
			this.host = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
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
	public final List<ServiceCommand> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public final ServiceProvider register(ServiceCommand descriptor) {
		commands.add(descriptor);
		return this;
	}

	@Override
	public ServiceDiscovery getServiceDiscovery() {
		return serviceDiscovery;
	}
	
	@Override
	public ServiceProvider serviceDiscovery(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
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
