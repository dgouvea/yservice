package yservice.core;

import spark.Request;
import spark.Response;

public class PersonService extends JsonService {

	@Override
	public String getName() {
		return "Person";
	}

	@Override
	public String getUri() {
		return "/person/:name";
	}

	@Override
	public int getPort() {
		return 4004;
	}

	@Override
	public Object run(Request req, Response res) {
		return new Person(req.params("name"), Integer.parseInt(req.queryParams("age")));
	}

	public static class Person {
		private String name;
		private int age;

		public Person() {
		}

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

}
