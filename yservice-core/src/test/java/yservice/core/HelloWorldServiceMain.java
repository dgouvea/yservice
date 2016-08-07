package yservice.core;

public class HelloWorldServiceMain extends ServiceServer {

	public static void main(String[] args) {
		HelloWorldService testService = new HelloWorldService();
		ServiceServer.init("http://localhost:8080/yservice", testService);
	}
	
}
