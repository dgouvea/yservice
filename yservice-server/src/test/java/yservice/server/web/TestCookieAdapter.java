package yservice.server.web;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.NewCookie;

import org.junit.Assert;
import org.junit.Test;

public class TestCookieAdapter {

	@Test
	public void testAdaptFromNewCookie() {
		NewCookie newCookie = new NewCookie("name", "value", "path", "domain", "comment", 1, true, false);
		
		CookieAdapter cookieAdapter = new CookieAdapter();
		Cookie cookie = cookieAdapter.adapt(newCookie);
		
		Assert.assertEquals(newCookie.getName(), cookie.getName());
		Assert.assertEquals(newCookie.getValue(), cookie.getValue());
		Assert.assertEquals(newCookie.getPath(), cookie.getPath());
		Assert.assertEquals(newCookie.getDomain(), cookie.getDomain());
		Assert.assertEquals(newCookie.getComment(), cookie.getComment());
		Assert.assertEquals(newCookie.getMaxAge(), cookie.getMaxAge());
		Assert.assertEquals(newCookie.isHttpOnly(), cookie.isHttpOnly());
		Assert.assertEquals(newCookie.isSecure(), cookie.getSecure());
	}

	@Test
	public void testAdaptFromCookie() {
		Cookie cookie = new Cookie("name", "value");
		cookie.setPath("path");
		cookie.setDomain("domain");
		cookie.setComment("comment");
		cookie.setMaxAge(1);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		
		CookieAdapter cookieAdapter = new CookieAdapter();
		NewCookie newCookie = cookieAdapter.adapt(cookie);
		
		Assert.assertEquals(cookie.getName(), newCookie.getName());
		Assert.assertEquals(cookie.getValue(), newCookie.getValue());
		Assert.assertEquals(cookie.getPath(), newCookie.getPath());
		Assert.assertEquals(cookie.getDomain(), newCookie.getDomain());
		Assert.assertEquals(cookie.getComment(), newCookie.getComment());
		Assert.assertEquals(cookie.getMaxAge(), newCookie.getMaxAge());
		Assert.assertEquals(cookie.isHttpOnly(), newCookie.isHttpOnly());
		Assert.assertEquals(cookie.getSecure(), newCookie.isSecure());
	}
	
}
