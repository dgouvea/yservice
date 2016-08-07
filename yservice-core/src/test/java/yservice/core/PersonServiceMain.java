package yservice.core;

public class PersonServiceMain extends ServiceServer {

	public static void main(String[] args) {
		PersonService service = new PersonService();
		ServiceServer.init("http://localhost:8080/yservice", service);
	}
	
}
