package yservice.service;

import spark.ResponseTransformer;
import yservice.service.transformer.Transformer;

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
