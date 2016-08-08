package yservice.service;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import yservice.core.Method;

public final class ServiceDiscovery {

	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";

	private static final String API_DISCOVERY_SERVICES_REGISTER = "/api/discovery/services/register";
	private static final String API_DISCOVERY_SERVICES_UNREGISTER = "/api/discovery/services/unregister";
	
	private final String host;

	private ServiceDiscovery(String host) {
		this.host = host;
	}
	
	public static ServiceDiscovery connect(String host) {
		return new ServiceDiscovery(host);
	}
	
	public HttpResponse<String> route(Method method, String uri) {
		return route(method, uri, null);
	}

	public HttpResponse<String> route(Method method, String uri, Object object) {
		Gson gson = new Gson();
		return route(method, uri, gson.toJson(object));
	}
	
	public HttpResponse<String> route(Method method, String uri, String body) {
		try {
			HttpRequest request;
			HttpMethod httpMethod = HttpMethod.valueOf(method.name());
			
			uri = "/route" + uri;
			
			if (body == null) {
				request = new HttpRequest(httpMethod, host + uri);
			} else {
				request = new HttpRequestWithBody(httpMethod, host + uri);
				((HttpRequestWithBody) request).body(body);
			}
			request.header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON);
			return request.asString();
		} catch (UnirestException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public int register(Service service) {
		Gson gson = new Gson();
		String json = gson.toJson(new ServiceRegistry(service.getServiceUrl(), service.getMethod().toString(), service.getUri()));
		
		HttpResponse<String> response;
		try {
			response = Unirest.put(host + API_DISCOVERY_SERVICES_REGISTER).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).body(json).asString();
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
			response = Unirest.put(host + API_DISCOVERY_SERVICES_UNREGISTER).header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON).body(json).asString();
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
