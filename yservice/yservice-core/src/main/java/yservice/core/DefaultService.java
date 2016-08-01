package yservice.core;

public abstract class DefaultService implements Service {

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public Method getMethod() {
		return Method.GET;
	}

}
