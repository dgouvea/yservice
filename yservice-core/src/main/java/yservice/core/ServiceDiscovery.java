package yservice.core;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class ServiceDiscovery {

	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";

	private static final String API_DISCOVERY_SERVICES_REGISTER = "/api/discovery/services/register";
	private static final String API_DISCOVERY_SERVICES_UNREGISTER = "/api/discovery/services/unregister";
	private static final String API_DISCOVERY_SERVICES_SERVICE = "/api/discovery/services/service";
	
	private final String host;

	private ServiceDiscovery(String host) {
		this.host = host;
	}
	
	public static ServiceDiscovery connect(String host) {
		return new ServiceDiscovery(host);
	}
	
	public String getHost() {
		return host;
	}
	
	public int register(ServiceRegistryDescriptor service) {
		Gson gson = new Gson();
		String json = gson.toJson(service);
		
		HttpResponse<String> response;
		try {
			response = Unirest.put(host.concat(API_DISCOVERY_SERVICES_REGISTER)).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).body(json).asString();
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response.getStatus();
	}

	public int unregister(ServiceRegistryDescriptor service) {
		Gson gson = new Gson();
		String json = gson.toJson(service);
		
		HttpResponse<String> response;
		try {
			response = Unirest.put(host.concat(API_DISCOVERY_SERVICES_UNREGISTER)).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).body(json).asString();
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response.getStatus();
	}
	
	public ServiceRegistryDescriptor service(String name, String version) {
		HttpResponse<ServiceRegistryDescriptor> response;
		try {
			response = Unirest.get(host + API_DISCOVERY_SERVICES_SERVICE + version + "/" + name).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).asObject(ServiceRegistryDescriptor.class);
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response.getBody();
	}
	
}
