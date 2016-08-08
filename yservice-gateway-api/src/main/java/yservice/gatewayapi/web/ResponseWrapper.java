/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class ResponseWrapper {

	private final int status;
	private final String reason;
	private final int contentLength;
	private final String body;
	private final Map<String, String> headers;
	private final List<Cookie> cookies;
	private final String toStringCached;
	
	public ResponseWrapper(int status, String reason, int contentLength, String body, Map<String, String> headers, List<Cookie> cookies) {
		this.status = status;
		this.reason = reason;
		this.contentLength = contentLength;
		this.body = body;
		this.headers = headers;
		this.cookies = cookies;
		this.toStringCached = generateToString();
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @return the contentLength
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * @return the body
	 */
	public final String getBody() {
		return body;
	}
	
	public boolean hasBody() {
		return body != null && !body.trim().isEmpty();
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
	public final List<Cookie> getCookies() {
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
		result = prime * result + contentLength;
		result = prime * result + ((cookies == null) ? 0 : cookies.hashCode());
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + status;
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
		ResponseWrapper other = (ResponseWrapper) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (contentLength != other.contentLength)
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
		if (status != other.status)
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
		string.append("Status: ").append(status).append(" - ").append(reason);
		
		if (!headers.isEmpty()) {
			string.append("\nHeaders:");
			headers.forEach((key, value) -> {
				string.append("\n\t").append(key).append("=").append(value);
			});
		}
		
		string.append("\nResponse body:\n\t").append(body);
		
		if (!cookies.isEmpty()) {
			string.append("\n\nCookies:");
			
			cookies.forEach(cookie -> {
				string.append("\n\t").append(cookie.getName()).append("=").append(cookie.getValue());
			});
		}
		
		return string.toString();
	}

	public static ResponseWrapperBuilder builder() {
		return new ResponseWrapperBuilder();
	}
	
	public static final class ResponseWrapperBuilder {

		private int status;
		private String reason;
		private int contentLength;
		private String body;
		private Map<String, String> headers = new HashMap<>();
		private List<Cookie> cookies = new ArrayList<>();
		
		private ResponseWrapperBuilder() {
			
		}
		
		public ResponseWrapperBuilder status(int status) {
			this.status = status;
			return this;
		}

		public ResponseWrapperBuilder reason(String reason) {
			this.reason = reason;
			return this;
		}

		public ResponseWrapperBuilder contentLength(int contentLength) {
			this.contentLength = contentLength;
			return this;
		}

		public ResponseWrapperBuilder body(String body) {
			this.body = body;
			return this;
		}

		public ResponseWrapperBuilder cookie(NewCookie cookie) {
			return cookie(new CookieAdapter().adapt(cookie));
		}

		public ResponseWrapperBuilder cookie(Cookie cookie) {
			this.cookies.add(cookie);
			return this;
		}

		public ResponseWrapperBuilder header(String name, String value) {
			this.headers.put(name, value);
			return this;
		}
		
		public ResponseWrapperBuilder response(Response response) throws IOException {
			// headers
			response.getHeaders().forEach((String key, List<Object> value) -> {
				header(key, (String) value.get(0));
			});

			// cookies
			CookieAdapter cookieAdapter = new CookieAdapter();
			response.getCookies().forEach((name, cookie) -> {
				cookie(cookieAdapter.adapt(cookie));
			});
			
			// status
			status(response.getStatus());
			reason(response.getStatusInfo().getReasonPhrase());
			
			// body
			if (response.hasEntity()) {
				body((String) response.readEntity(String.class));
			}
			
			contentLength(response.getLength());
			
			return this;
		}
		
		public ResponseWrapper build() {
			return new ResponseWrapper(status, reason, contentLength, body, headers, cookies);
		}
		
	}
	
}
