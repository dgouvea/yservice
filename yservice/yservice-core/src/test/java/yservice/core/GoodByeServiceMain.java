package yservice.core;

public class GoodByeServiceMain extends ServiceServer {

	public static void main(String[] args) {
		GoodByeService testService = new GoodByeService();
		ServiceServer.init(testService);
	}
	
}
