package yservice.service;

import spark.ResponseTransformer;
import yservice.service.transformer.Transformer;

public interface JsonServiceCommand extends DefaultServiceCommand {

	@Override
	default String getContentType() {
		return "application/json";
	}
	
	@Override
	default ResponseTransformer getTransformer() {
		return Transformer.json();
	}
	
}
