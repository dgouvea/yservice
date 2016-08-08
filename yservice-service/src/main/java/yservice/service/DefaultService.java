package yservice.service;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import yservice.core.Method;

public abstract class DefaultService implements Service {

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public Method getMethod() {
		return Method.GET;
	}

	@Override
	public String getHost() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
