package yservice.core;

public class HelloWorld2ServiceMain extends ServiceServer {

	public static void main(String[] args) {
		HelloWorld2Service testService = new HelloWorld2Service();
		ServiceServer.init(testService);
	}
	
}
