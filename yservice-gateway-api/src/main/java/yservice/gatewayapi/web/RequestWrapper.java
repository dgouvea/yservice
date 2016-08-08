/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;

import yservice.core.Method;

public class RequestWrapper {

	private final Method method;
	private final String uri;
	private final String body;
	private final String contentType;
	private final Map<String, String> parameters;
	private final Map<String, String> headers;
	private final List<NewCookie> cookies;
	private final String toStringCached;
	
	public RequestWrapper(Method method, String uri, String body, String contentType, Map<String, String> parameters, Map<String, String> headers, List<NewCookie> cookies) {
		this.method = method;
		this.uri = uri;
		this.body = body;
		this.contentType = contentType;
		this.parameters = parameters;
		this.headers = headers;
		this.cookies = cookies;
		this.toStringCached = generateToString();
	}

	/**
	 * @return the method
	 */
	public final Method getMethod() {
		return method;
	}

	/**
	 * @return the uri
	 */
	public final String getUri() {
		return uri;
	}

	/**
	 * @return the body
	 */
	public final String getBody() {
		return body;
	}

	public final boolean hasBody() {
		return body != null && !body.trim().isEmpty();
	}
	
	/**
	 * @return the contentType
	 */
	public final String getContentType() {
		return contentType;
	}

	/**
	 * @return the parameters
	 */
	public final Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	public final String getQueryString() {
		if (parameters.isEmpty() || method.isBodyAllowed()) {
			return "";
		}
		StringBuilder queryString = new StringBuilder();
		queryString.append("?");
		parameters.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).forEach(queryString::append);
		return queryString.toString();
	}
	
	/**
	 * @return the headers
	 */
	public final Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * @return the cookies
	 */
	public final List<NewCookie> getCookies() {
		return Collections.unmodifiableList(cookies);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((cookies == null) ? 0 : cookies.hashCode());
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestWrapper other = (RequestWrapper) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (cookies == null) {
			if (other.cookies != null)
				return false;
		} else if (!cookies.equals(other.cookies))
			return false;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (method != other.method)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toStringCached;
	}
	
	private String generateToString() {
		StringBuilder string = new StringBuilder();
		string.append(method).append(" ").append(uri);
		
		if (!headers.isEmpty()) {
			string.append("\nHeaders:");
			headers.forEach((key, value) -> {
				string.append("\n\t").append(key).append("=").append(value);
			});
		}

		if (!parameters.isEmpty()) {
			string.append("\nParameters:");
			parameters.forEach((key, value) -> {
				string.append("\n\t").append(key).append("=").append(value);
			});
		}
		
		string.append("\nRequest body:\n\t").append(body);
		
		if (!cookies.isEmpty()) {
			string.append("\n\nCookies:");
			
			cookies.forEach(cookie -> {
				string.append("\n\t").append(cookie.getName()).append("=").append(cookie.getValue());
			});
		}
		
		return string.toString();
	}

	public static RequestWrapperBuilder builder() {
		return new RequestWrapperBuilder();
	}
	
	public static final class RequestWrapperBuilder {

		private Method method;
		private String uri;
		private String body;
		private String contentType;
		private Map<String, String> parameters = new HashMap<>();
		private Map<String, String> headers = new HashMap<>();
		private List<NewCookie> cookies = new ArrayList<>();
		
		private RequestWrapperBuilder() {
			
		}
		
		public RequestWrapperBuilder method(String method) {
			return method(Method.from(method));
		}
		
		public RequestWrapperBuilder method(Method method) {
			this.method = method;
			return this;
		}

		public RequestWrapperBuilder uri(String uri) {
			this.uri = uri;
			return this;
		}

		public RequestWrapperBuilder body(String body) {
			this.body = body;
			return this;
		}

		public RequestWrapperBuilder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public RequestWrapperBuilder cookie(NewCookie cookie) {
			this.cookies.add(cookie);
			return this;
		}

		public RequestWrapperBuilder cookie(Cookie cookie) {
			return cookie(new CookieAdapter().adapt(cookie));
		}

		public RequestWrapperBuilder header(String name, String value) {
			this.headers.put(name, value);
			return this;
		}

		public RequestWrapperBuilder param(String name, String value) {
			this.parameters.put(name, value);
			return this;
		}
		
		public RequestWrapperBuilder request(HttpServletRequest req) throws IOException {
			method(Method.from(req.getMethod()));
			uri(req.getPathInfo());
			contentType(req.getContentType());
			
			// body
			StringBuilder body = new StringBuilder();
			req.getReader().lines().forEach(body::append);
			body(body.toString());
			
			// parameters
			Enumeration<String> parameterNames = req.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				param(name, req.getParameter(name));
			}

			// headers
			Enumeration<String> headerNames = req.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				header(name, req.getHeader(name));
			}
			
			// cookies
			CookieAdapter cookieAdapter = new CookieAdapter();
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					cookie(cookieAdapter.adapt(cookie));
				}
			}
			
			return this;
		}
		
		public RequestWrapper build() {
			return new RequestWrapper(method, uri, body, contentType, parameters, headers, cookies);
		}
		
	}
	
}
