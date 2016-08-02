package yservice.core;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;
import yservice.core.transformer.Transformer;

public class ServiceMain {

	private static final Logger logger = LoggerFactory.getLogger(ServiceMain.class);
	
	public static void main(String[] args) {
		Service service = new TestService();
		
		Route route = (req, res) -> {
			return service.run(req, res);
		};
		
		Spark.exception(Exception.class, (e, req, res) -> {
			logger.error(e.getMessage(), e);
		});
		
		try {
			int port = service.getPort();
			port(port);
			logger.info("Service " + service.getName() + " starting at " + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = service.getUrl();
		if (url == null || url.trim().isEmpty()) {
			throw new IllegalArgumentException("URL cannot be null or empty");
		}
		
		if (!url.startsWith("/")) {
			url = "/" + url;
		}
		
		ResponseTransformer transformer = service.getTransformer();
		boolean isTransformable = transformer != null;
		
		if (service.getMethod().isGet()) {
			if (isTransformable) {
				get(url, route, transformer);
			} else {
				get(url, route);
			}
		} else if (service.getMethod().isPost()) {
			if (isTransformable) {
				post(url, route, transformer);
			} else {
				post(url, route);
			}
		} else if (service.getMethod().isPut()) {
			if (isTransformable) {
				put(url, route, transformer);
			} else {
				put(url, route);
			}
		} else if (service.getMethod().isDelete()) {
			if (isTransformable) {
				delete(url, route, transformer);
			} else {
				delete(url, route);
			}
		} else if (service.getMethod().isTrace()) {
			if (isTransformable) {
				trace(url, route, transformer);
			} else {
				trace(url, route);
			}
		} else if (service.getMethod().isHead()) {
			if (isTransformable) {
				head(url, route, transformer);
			} else {
				head(url, route);
				}
		} else if (service.getMethod().isOptions()) {
			if (isTransformable) {
				options(url, route, transformer);
			} else {
				options(url, route);
			}
		}
		
		get("/", (req, res) -> {
			return "OK";
		});
		
		get("/service/info", (req, res) -> {
			Runtime runtime = Runtime.getRuntime();
			return new ServiceInfo(service.getName(), service.getVersion(), service.getUrl(), runtime.totalMemory(), runtime.freeMemory());
		}, Transformer.json());
		
		delete("/service/stop", (req, res) -> {
			Spark.stop();
			return "OK";
		});
	}
	
	public static class ServiceInfo {
		
		private final String name;
		private final String url;
		private final String version;
		private final long totalMemory;
		private final long freeMemory;
		private final long memory;
		
		public ServiceInfo(String name, String version, String url, long totalMemory, long freeMemory) {
			this.name = name;
			this.version = version;
			this.url = url;
			this.totalMemory = totalMemory;
			this.freeMemory = freeMemory;
			this.memory = totalMemory - freeMemory;
		}
		
		public String getName() {
			return name;
		}
		
		public String getVersion() {
			return version;
		}
		
		public String getUrl() {
			return url;
		}
		
		public long getTotalMemory() {
			return totalMemory;
		}
		
		public long getFreeMemory() {
			return freeMemory;
		}
		
		public long getMemory() {
			return memory;
		}
		
	}
	
}
