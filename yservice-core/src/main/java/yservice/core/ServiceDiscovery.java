package yservice.core;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class ServiceDiscovery {

	private final String host;

	private ServiceDiscovery(String host) {
		this.host = host;
	}
	
	public static ServiceDiscovery connect(String host) {
		return new ServiceDiscovery(host);
	}
	
	public int register(Service service) {
		Gson gson = new Gson();
		String json = gson.toJson(new ServiceRegistry(service.getServiceUrl(), service.getMethod().toString(), service.getUri()));
		
		HttpResponse<String> response;
		try {
			response = Unirest.put(host + "/api/discovery/service/register").header("Content-Type", "application/json").header("Accept", "application/json").body(json).asString();
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response.getStatus();
	}

	public int unregister(Service service) {
		Gson gson = new Gson();
		String json = gson.toJson(new ServiceRegistry(service.getServiceUrl(), service.getMethod().toString(), service.getUri()));
		
		HttpResponse<String> response;
		try {
			response = Unirest.put(host + "/api/discovery/service/unregister").header("Content-Type", "application/json").header("Accept", "application/json").body(json).asString();
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response.getStatus();
	}
	
	static class ServiceRegistry {
		
		private final String domain;
		private final String method;
		private final String uri;

		private ServiceRegistry(String domain, String method, String uri) {
			this.domain = domain;
			this.method = method;
			this.uri = uri;
		}
		
		public String getDomain() {
			return domain;
		}
		
		public String getMethod() {
			return method;
		}
		
		public String getUri() {
			return uri;
		}

	}
	
}
