package project.tosstock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(TestContainerConfig.class)
public abstract class IntegrationTestSupport {

}
