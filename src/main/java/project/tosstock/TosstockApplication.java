package project.tosstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TosstockApplication {

	public static void main(String[] args) {
		SpringApplication.run(TosstockApplication.class, args);
	}

}
