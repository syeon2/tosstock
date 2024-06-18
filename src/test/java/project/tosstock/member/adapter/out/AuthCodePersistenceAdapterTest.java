package project.tosstock.member.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import project.tosstock.IntegrationTestSupport;

class AuthCodePersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private VerificationEmailCodePersistenceAdapter authCodePersistenceAdapter;

	@Autowired
	@Qualifier(value = "redisVerificationEmailCodeTemplate")
	private RedisTemplate<String, String> redisVerificationEmailCodeTemplate;

	@BeforeEach
	void before() {
		redisVerificationEmailCodeTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@Test
	@DisplayName(value = "이메일과 인증 번호를 저장소에 저장합니다.")
	void save() {
		// given
		String email = "waterki93@gmail.com";
		String code = "123456";

		// when
		authCodePersistenceAdapter.save(email, code);

		// then
		String findCode = redisVerificationEmailCodeTemplate.opsForValue().get(email);

		assertThat(findCode).isEqualTo(code);
	}

	@Test
	@DisplayName(value = "이메일로 저장된 인증 번호를 조회합니다.")
	void findAuthCodeByMail() {
		// given
		String email = "waterki93@gmail.com";
		String code = "123456";

		authCodePersistenceAdapter.save(email, code);

		// when
		Optional<String> findAuthCode = authCodePersistenceAdapter.findAuthCodeByMail(email);

		// then
		assertThat(findAuthCode).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(code));
	}
}
