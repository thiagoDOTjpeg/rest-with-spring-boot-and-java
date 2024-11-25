package br.com.gritti;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	private static final String template = "Olá %s, você tem %d!";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping("/users")
	public Users users(@RequestParam String name, Integer age) {
		return new Users(counter.incrementAndGet(), name, age, String.format(template, name, age));
	}
}
