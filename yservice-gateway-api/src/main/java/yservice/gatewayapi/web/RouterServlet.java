/*
 * Copyright (c) 2016 Definity Team <developer@definitysolutions.com>
 * MIT Licensed
 */
package yservice.gatewayapi.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yservice.gatewayapi.web.processor.DefaultProcessExecutor;
import yservice.gatewayapi.web.processor.LoggerProcessExecutor;
import yservice.gatewayapi.web.processor.ProcessExecutor;

/**
 * Servlet used to route requests to respective REST services.
 * 
 * @author David Sobreira Gouvea
 */
public class RouterServlet extends HttpServlet {

	private static final long serialVersionUID = 4018933413518793704L;
	
	private final ProcessExecutor processExecutor;

	/**
	 * Constructs a new router Servlet.
	 * Defining {@code LoggerProcessExecutor} process executor as default. 
	 */
	public RouterServlet() {
		processExecutor = new LoggerProcessExecutor();
		processExecutor.setNext(new DefaultProcessExecutor());
	}
	
	/**
	 * Constructs a new router Servlet with a given process executor.
	 * 
	 * @param processExecutor process executor
	 */
	public RouterServlet(ProcessExecutor processExecutor) {
		this.processExecutor = processExecutor;
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
