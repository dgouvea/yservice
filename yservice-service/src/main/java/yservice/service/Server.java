package yservice.service;

import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.ipAddress;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.secure;
import static spark.Spark.trace;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;
import yservice.core.ServiceDiscovery;
import yservice.service.transformer.Transformer;

public final class Server {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private Server() {
		
	}
	
	public static void init(ServiceProvider serviceProvider, String... args) {
		ServiceLoggerManager.getInstance();

		ServiceParameter param = new ServiceParameter();
		param.setIp(serviceProvider.getHost());
		param.setPort(serviceProvider.getPort());
		param.setThreads(serviceProvider.getThreads());
		param.parse(args);

		ipAddress(param.getIp());
		port(param.getPort());
		logger.info("Service " + serviceProvider.getName() + " starting at " + param.getPort());
		
		Spark.exception(Exception.class, (e, req, res) -> {
			logger.error(e.getMessage(), e);
		});

		if (param.getKeyStoreLocation() != null && param.getKeyStorePassword() != null) {
			secure(param.getKeyStoreLocation(), param.getKeyStorePassword(), null, null);
		}
		
		if (param.getThreads() > 0) {
			Spark.threadPool(param.getThreads());
		}
		
		List<ServiceCommand> commands = serviceProvider.getCommands();
		commands.forEach(service -> {
			String uri = service.getUri();
			if (uri == null || uri.trim().isEmpty()) {
				throw new IllegalArgumentException("URI cannot be null or empty");
			}
			
			if (!uri.startsWith("/")) {
				uri = "/".concat(uri);
			}
			
			after((req, res) -> {
				if (param.isGzip()) {
					res.header("Content-Encoding", "gzip");
				}
				if (param.getContentType() != null) {
					res.header("Content-Type", param.getContentType());
				}
			});
			
			serviceRoute(service, uri);
			serviceBasicRoute(serviceProvider, service, serviceProvider.getServiceDiscovery());
			
			if (serviceProvider.getServiceDiscovery() != null) {
				serviceProvider.getServiceDiscovery().register(serviceProvider.getDescriptor());
			}
		});
	}

	private static void serviceBasicRoute(ServiceProvider serviceProvider, ServiceCommand service, ServiceDiscovery serviceDiscovery) {
		head("/service/health", (req, res) -> {
			return "OK";
		});

		get("/service/info", (req, res) -> {
			Runtime runtime = Runtime.getRuntime();
			return new ServiceInfo(serviceProvider.getName(), serviceProvider.getVersion(), service.getUri(), runtime.totalMemory(),
					runtime.freeMemory());
		} , Transformer.json());

		get("/service/log", (req, res) -> {
			return ServiceLoggerManager.getInstance().getLog();
		});

		delete("/service/stop", (req, res) -> {
			if (serviceDiscovery != null) {
				serviceDiscovery.unregister(serviceProvider.getDescriptor());
			}

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
	}

	private static void serviceRoute(ServiceCommand service, String uri) {
		Route route = (req, res) -> {
			return service.execute(req, res);
		};

		ResponseTransformer transformer = service.getTransformer();
		boolean isTransformable = transformer != null;
		if (service.getMethod().isGet()) {
			if (isTransformable) {
				get(uri, route, transformer);
			} else {
				get(uri, route);
			}
		} else if (service.getMethod().isPost()) {
			if (isTransformable) {
				post(uri, route, transformer);
			} else {
				post(uri, route);
			}
		} else if (service.getMethod().isPut()) {
			if (isTransformable) {
				put(uri, route, transformer);
			} else {
				put(uri, route);
			}
		} else if (service.getMethod().isDelete()) {
			if (isTransformable) {
				delete(uri, route, transformer);
			} else {
				delete(uri, route);
			}
		} else if (service.getMethod().isTrace()) {
			if (isTransformable) {
				trace(uri, route, transformer);
			} else {
				trace(uri, route);
			}
		} else if (service.getMethod().isHead()) {
			if (isTransformable) {
				head(uri, route, transformer);
			} else {
				head(uri, route);
			}
		} else if (service.getMethod().isOptions()) {
			if (isTransformable) {
				options(uri, route, transformer);
			} else {
				options(uri, route);
			}
		}
	}

	static class ServiceParameter {
		private String ip;
		private int port;
		private int threads;
		private boolean gzip;
		private String contentType;
		private String keyStoreLocation;
		private String keyStorePassword;
		
		public void parse(String[] args) {
			for (int i = 0; i < args.length; i++) {
				String param = args[i].trim();
				if (!param.startsWith("-"))
					continue;
				
				if (param.equals("-ip")) {
					setIp(args[++i]);
				} else if (param.equals("-port")) {
					setPort(Integer.parseInt(args[++i]));
				} else if (param.equals("-threads")) {
					setThreads(Integer.parseInt(args[++i]));
				} else if (param.equals("-gzip")) {
					setGzip(true);
				} else if (param.equals("-contentType")) {
					setContentType(args[++i]);
				} else if (param.equals("-ssl")) {
					setKeyStoreLocation(args[++i]);
					setKeyStorePassword(args[++i]); // TODO: remove from args
				}
			}
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getThreads() {
			return threads;
		}

		public void setThreads(int threads) {
			this.threads = threads;
		}
		
		public boolean isGzip() {
			return gzip;
		}
		
		public void setGzip(boolean gzip) {
			this.gzip = gzip;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getKeyStoreLocation() {
			return keyStoreLocation;
		}

		public void setKeyStoreLocation(String keyStorelocation) {
			this.keyStoreLocation = keyStorelocation;
		}

		public String getKeyStorePassword() {
			return keyStorePassword;
		}

		public void setKeyStorePassword(String keyStorepassword) {
			this.keyStorePassword = keyStorepassword;
		}

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
			LoggerContext context = LoggerContext.getContext(false);
			Configuration cfg = context.getConfiguration();

			Layout<?> layout = null;

			LoggerConfig rootLogger = cfg.getRootLogger();
			if (!rootLogger.getAppenderRefs().isEmpty()) {
				Optional<Appender> firstAppender = rootLogger.getAppenders().values().stream().findFirst();
				if (firstAppender.isPresent()) {
					layout = firstAppender.get().getLayout();
				}
			}

			if (layout == null) {
				layout = PatternLayout.createDefaultLayout(cfg);
			}

			OutputStreamAppender appender = OutputStreamAppender.createAppender(layout, null, bos, "", true, false);
			appender.start();
			cfg.addAppender(appender);

			rootLogger.addAppender(appender, Level.TRACE, rootLogger.getFilter());
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
