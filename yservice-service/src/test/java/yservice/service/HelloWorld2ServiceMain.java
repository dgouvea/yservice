package yservice.service;

import yservice.service.ServiceServer;

public class HelloWorld2ServiceMain extends ServiceServer {

	public static void main(String[] args) {
		HelloWorld2Service testService = new HelloWorld2Service();
		ServiceServer.init("http://localhost:8080/yservice", testService);
	}
	
}
