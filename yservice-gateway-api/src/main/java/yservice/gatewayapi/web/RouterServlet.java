/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yservice.core.ServiceDiscovery;
import yservice.gatewayapi.web.processor.DefaultProcessExecutor;
import yservice.gatewayapi.web.processor.ProcessExecutor;

/**
 * Servlet used to route requests to respective REST services.
 * 
 * @author David Sobreira Gouvea
 */
public class RouterServlet extends HttpServlet {

	private static final long serialVersionUID = 4018933413518793704L;
	
	private ProcessExecutor processExecutor;

	@Override
	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String serviceDiscoveryHost = config.getInitParameter("serviceDiscovery");
		if (serviceDiscoveryHost == null || serviceDiscoveryHost.isEmpty()) {
			throw new ServletException("servlet init parameter serviceDiscovery must be defined");
		}
		
		ServiceDiscovery serviceDiscovery = ServiceDiscovery.connect(serviceDiscoveryHost);

		String processExecutorClassName = config.getInitParameter("processExecutor");
		if (processExecutorClassName != null && !processExecutorClassName.isEmpty()) {
			Class<? extends ProcessExecutor> processExecutorClass;
			try {
				processExecutorClass = (Class<? extends ProcessExecutor>) Class.forName(processExecutorClassName);
				processExecutor = processExecutorClass.getConstructor(ServiceDiscovery.class).newInstance(serviceDiscovery);
			} catch (ClassNotFoundException e) {
				throw new ServletException("Process executor not found: " + processExecutorClassName, e);
			} catch (InstantiationException e) {
				throw new ServletException("Process executor cannot be instanciated: " + processExecutorClassName, e);
			} catch (IllegalAccessException e) {
				throw new ServletException("Process executor has illegal access modifier: " + processExecutorClassName, e);
			} catch (IllegalArgumentException e) {
				throw new ServletException("Process executor has illegal argument: " + processExecutorClassName, e);
			} catch (InvocationTargetException e) {
				throw new ServletException("Process executor has a problem to be instanciated: " + processExecutorClassName, e);
			} catch (NoSuchMethodException e) {
				throw new ServletException("Process executor has no constructor with ServiceDiscovery parameter: " + processExecutorClassName, e);
			} catch (SecurityException e) {
				throw new ServletException("Process executor has a security problem: " + processExecutorClassName, e);
			}
		} else {
			processExecutor = new DefaultProcessExecutor(serviceDiscovery);
		}
	}
	
	/**
	 * Route request to REST service.
	 * 
	 * @param req the request
	 * @param resp the response
	 * @throw IOException if an input or output error occurs while the HTTP request was being handled
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// request
		RequestWrapper requestWrapper = RequestWrapper.builder().request(req).build();
		
		// execute
		ResponseWrapper responseWrapper = processExecutor.execute(requestWrapper);
		
		// response
		defineResponse(resp, responseWrapper);
	}

	/**
	 * Define the servlet response based on {@code ResponseWrapper}.
	 * 
	 * @param resp the servlet response
	 * @param response the response wrapper
	 * @throws IOException if an input or output error occurs while the HTTP request was being handled
	 */
	protected void defineResponse(HttpServletResponse resp, ResponseWrapper response) throws IOException {
		// headers
		response.getHeaders().forEach(resp::setHeader);

		// cookies
		response.getCookies().forEach(resp::addCookie);
		
		// status
		resp.setStatus(response.getStatus());
		
		// body
		if (response.hasBody()) {
			resp.getWriter().write(response.getBody());
		}
		
		// content length
		resp.setContentLength(response.getContentLength());
		resp.flushBuffer();
	}
	
}
