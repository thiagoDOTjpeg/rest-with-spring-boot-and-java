package br.com.gritti;

public class Users {
	private final long id;
	private final String name;
	private final Integer age;
	private final String message;
	
	public Users(long id, String name, Integer age, String message) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}
	public String getMessage() {
		return message;
	}
}
