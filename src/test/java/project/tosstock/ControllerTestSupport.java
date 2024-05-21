package project.tosstock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected ObjectMapper objectMapper;
}
