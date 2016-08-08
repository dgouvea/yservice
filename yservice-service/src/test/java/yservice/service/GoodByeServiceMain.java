package yservice.service;

import yservice.service.ServiceServer;

public class GoodByeServiceMain extends ServiceServer {

	public static void main(String[] args) {
		GoodByeService testService = new GoodByeService();
		ServiceServer.init("http://localhost:8080/yservice", testService);
	}
	
}
