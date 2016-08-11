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
			
			uri = "/route".concat(uri);
			
			if (body == null) {
				request = new HttpRequest(httpMethod, host.concat(uri));
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
