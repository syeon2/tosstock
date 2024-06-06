package project.tosstock.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;

class RedisAuthCodeRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private RedisAuthCodeRepository redisAuthCodeRepository;

	@Test
	void save_auth_code() {
		// given
		String email = "waterkite94@gmail.com";
		String authCode = "123456";

		// when
		redisAuthCodeRepository.save(email, authCode);

		// then
		Optional<String> codeOptional = redisAuthCodeRepository.findCodeByEmail(email);

		assertThat(codeOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(authCode));
	}
}
