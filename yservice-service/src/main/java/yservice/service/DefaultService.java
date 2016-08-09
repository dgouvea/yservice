package yservice.service;

import yservice.core.Method;

public interface DefaultService extends Service {

	@Override
	default Method getMethod() {
		return Method.GET;
	}
	
}
