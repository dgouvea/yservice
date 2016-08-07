package yservice.core;

import spark.ResponseTransformer;
import yservice.core.transformer.Transformer;

public abstract class JsonService extends DefaultService {

	@Override
	public String getContentType() {
		return "application/json";
	}
	
	@Override
	public final ResponseTransformer getTransformer() {
		return Transformer.json();
	}
	
}
