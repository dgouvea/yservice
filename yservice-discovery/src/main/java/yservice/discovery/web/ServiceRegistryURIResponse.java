package yservice.discovery.web;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceRegistryURIResponse {

	private List<String> uris;

	public ServiceRegistryURIResponse() {

	}
	
	public ServiceRegistryURIResponse(List<String> uris) {
		this.uris = uris;
	}

	public List<String> getUris() {
		return uris;
	}

	public void setUris(List<String> uris) {
		this.uris = uris;
	}
	
}
