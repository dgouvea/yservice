package yservice.service;

import yservice.service.ServiceServer;

public class HelloServiceMain {

	public static void main(String[] args) {
		HelloService service = new HelloService();
		ServiceServer.init("http://localhost:8080/yservice", service);
	}
	
}
