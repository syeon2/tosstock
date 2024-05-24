package project.tosstock;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerConfig implements BeforeAllCallback {

	private static final String REDIS_AUTH_CODE_IMAGE = "redis:latest";
	private static final int REDIS_AUTH_CODE_PORT = 6379;
	private GenericContainer redisForAuthCode;

	@Override
	public void beforeAll(ExtensionContext context) {
		/*
		 * Redis Auth Code To Join Member
		 */
		redisForAuthCode = new GenericContainer(DockerImageName.parse(REDIS_AUTH_CODE_IMAGE))
			.withExposedPorts(REDIS_AUTH_CODE_PORT);
		redisForAuthCode.start();

		System.setProperty("spring.data.redis_mail.host", redisForAuthCode.getHost());
		System.setProperty("spring.data.redis_mail.port",
			String.valueOf(redisForAuthCode.getMappedPort(REDIS_AUTH_CODE_PORT
			)));
	}
}
