/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.NewCookie;

/**
 * This class is responsible to adapt two different Cookie types.
 * 
 * @author David Sobreira Gouvea
 * @see Adapter Design Pattern
 */
public class CookieAdapter {

	/**
	 * Adapt a {@code NewCookie} to {@code Cookie}.
	 * <p>Returns a new instance of {@code Cookie} based on given {@code NewCookie}</p>
	 * 
	 * @param newCookie the {@code NewCookie} object.
	 * @return a new instance of {@code Cookie} based on given {@code NewCookie}
	 */
	public Cookie adapt(NewCookie newCookie) {
		Cookie cookie = new Cookie(newCookie.getName(), newCookie.getValue());
		cookie.setComment(newCookie.getComment());
		cookie.setDomain(newCookie.getDomain());
		cookie.setMaxAge(newCookie.getMaxAge());
		cookie.setVersion(newCookie.getVersion());
		cookie.setPath(newCookie.getPath());
		cookie.setHttpOnly(newCookie.isHttpOnly());
		cookie.setSecure(newCookie.isSecure());
		return cookie;
	}

	/**
	 * Adapt a {@code Cookie} to {@code NewCookie}.
	 * <p>Returns a new instance of {@code NewCookie} based on given {@code Cookie}</p>
	 * 
	 * @param newCookie the {@code NewCookie} object.
	 * @return a new instance of {@code NewCookie} based on given {@code Cookie}
	 */
	public NewCookie adapt(Cookie cookie) {
		return new NewCookie(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(), cookie.isHttpOnly());
	}

}
