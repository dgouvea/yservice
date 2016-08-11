package yservice.service;

import yservice.core.Method;

public interface DefaultServiceCommand extends ServiceCommand {

	@Override
	default Method getMethod() {
		return Method.GET;
	}
	
}
