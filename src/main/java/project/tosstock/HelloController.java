package project.tosstock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/api/v1/testing")
	public String testing() {
		return "hello";
	}
}
