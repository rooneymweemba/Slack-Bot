package primebot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
public class PrimebotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimebotApplication.class, args);
	}
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
}
