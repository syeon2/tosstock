package project.tosstock.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;

class RedisVerificationEmailCodeRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private RedisVerificationEmailCodeRepository redisVerificationEmailCodeRepository;

	@Test
	@DisplayName(value = "코드를 저장하고 조회합니다.")
	void saveAndFindCode() {
		// given
		String email = "waterkite94@gmail.com";
		String authCode = "123456";

		// when
		redisVerificationEmailCodeRepository.save(email, authCode);

		// then
		Optional<String> codeOptional = redisVerificationEmailCodeRepository.findCodeByEmail(email);

		assertThat(codeOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(authCode));
	}
}
