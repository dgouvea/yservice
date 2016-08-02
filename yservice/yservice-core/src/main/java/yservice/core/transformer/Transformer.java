package yservice.core.transformer;

import spark.ResponseTransformer;

public final class Transformer {

	private Transformer() {
		
	}
	
	public static synchronized ResponseTransformer json() {
		return new JsonTransformer();
	}
	
}
