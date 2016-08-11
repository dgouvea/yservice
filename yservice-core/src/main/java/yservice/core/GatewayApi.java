package yservice.core;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class GatewayApi {

	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	
	private final String host;

	private GatewayApi(String host) {
		this.host = host;
	}
	
	public static GatewayApi connect(String host) {
		return new GatewayApi(host);
	}
	
	public GatewayServiceRouter route(String service, String version) {
		return new GatewayServiceRouter(service, version);
	}
	
	public HttpResponse<String> execute(Method method, String service, String version, String uri) {
		return route(service, version).execute(method, uri);
	}

	public HttpResponse<String> execute(Method method, String service, String version, String uri, Object object) {
		return route(service, version).execute(method, uri, object);
	}

	public HttpResponse<String> execute(Method method, String service, String version, String uri, String body) {
		return route(service, version).execute(method, uri, body);
	}
	
	public class GatewayServiceRouter {
		
		private final String service;
		private final String version;
		
		private GatewayServiceRouter(String service, String version) {
			this.service = service;
			this.version = version;
		}
		
		public HttpResponse<String> execute(Method method, String uri) {
			return execute(method, uri, null);
		}

		public HttpResponse<String> execute(Method method, String uri, Object object) {
			Gson gson = new Gson();
			return execute(method, uri, gson.toJson(object));
		}
		
		public HttpResponse<String> execute(Method method, String uri, String body) {
			try {
				HttpRequest request;
				HttpMethod httpMethod = HttpMethod.valueOf(method.name());
				
				String serviceURI = "/route/" + version + "/" + service + uri;
				
				if (body == null) {
					request = new HttpRequest(httpMethod, host.concat(serviceURI));
				} else {
					request = new HttpRequestWithBody(httpMethod, host.concat(uri));
					((HttpRequestWithBody) request).body(body);
				}
				request.header(CONTENT_TYPE, APPLICATION_JSON).header(ACCEPT, APPLICATION_JSON);
				return request.asString();
			} catch (UnirestException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
	}
	
}
