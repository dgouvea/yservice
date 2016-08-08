package yservice.discovery.web;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import yservice.core.ServiceRegistryDescriptor;

@XmlRootElement
public class ServiceRegistryResponse {

	private List<ServiceRegistryDescriptor> services;

	public ServiceRegistryResponse() {

	}

	public ServiceRegistryResponse(List<ServiceRegistryDescriptor> services) {
		this.services = services;
	}

	public List<ServiceRegistryDescriptor> getServices() {
		return services;
	}

	public void setServices(List<ServiceRegistryDescriptor> services) {
		this.services = services;
	}
	
}
