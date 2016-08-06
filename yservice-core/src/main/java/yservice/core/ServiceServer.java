package yservice.core;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.trace;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;
import yservice.core.transformer.Transformer;

public class ServiceServer {

	private static final Logger logger = LoggerFactory.getLogger(ServiceServer.class);
	
	public static void init(Service service) {
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
		
		String url = service.getUri();
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
		
		ServiceDiscovery discovery = ServiceDiscovery.connect("http://localhost:8080/yservice");
		
		get("/", (req, res) -> {
			return "OK";
		});
		
		get("/service/info", (req, res) -> {
			Runtime runtime = Runtime.getRuntime();
			return new ServiceInfo(service.getName(), service.getVersion(), service.getUri(), runtime.totalMemory(), runtime.freeMemory());
		}, Transformer.json());

		get("/service/log", (req, res) -> {
			return ServiceLoggerManager.getInstance().getLog();
		});
		
		delete("/service/stop", (req, res) -> {
			discovery.unregister(service);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Spark.stop();
					
					this.cancel();
					timer.cancel();
				}
			}, 500);
			
			return "OK";
		});
		
		discovery.register(service);
	}
	
	static class ServiceLoggerManager {

		private static volatile ServiceLoggerManager instance;
		
		private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		private ServiceLoggerManager() {
			
		}
		
		static ServiceLoggerManager getInstance() {
			if (instance == null) {
				synchronized (ServiceLoggerManager.class) {
					instance = new ServiceLoggerManager();
					instance.init();
				}
			}
			return instance;
		}
		
		private void init() {
			//OutputStreamManager.getManager("SYSTEM_OUT.false.false-1", data, factory);
		}
		
		synchronized String getLog() {
			return new String(bos.toByteArray());
		}
		
	}
	
	public static class ServiceInfo {
		
		private final String name;
		private final String url;
		private final String version;
		private final long totalMemory;
		private final long freeMemory;
		private final long memory;
		
		ServiceInfo(String name, String version, String url, long totalMemory, long freeMemory) {
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
