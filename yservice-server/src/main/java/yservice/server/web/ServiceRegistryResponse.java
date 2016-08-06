package yservice.server.web;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceRegistryResponse {

	private List<ServiceRegistryRequest> services;

	public ServiceRegistryResponse() {

	}

	public ServiceRegistryResponse(List<ServiceRegistryRequest> services) {
		this.services = services;
	}

	public List<ServiceRegistryRequest> getServices() {
		return services;
	}

	public void setServices(List<ServiceRegistryRequest> services) {
		this.services = services;
	}
	
}
