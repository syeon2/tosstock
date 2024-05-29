package project.tosstock.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;

class RedisAuthTokenRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private RedisAuthTokenRepository redisAuthTokenRepository;

	@Test
	@DisplayName(value = "Email(key), Address(hashKey), Token(value) 을 가진 Map 자료구조로 저장합니다.")
	void save() {
		// given
		String email = "waterkite94@gmail.com";
		String token = "token";
		String address = "1";

		// when
		redisAuthTokenRepository.save(email, address, token);

		// then
		Optional<String> findTokenByEmailAndAddress
			= redisAuthTokenRepository.findTokenByEmailAndAddress(email, address);

		assertThat(findTokenByEmailAndAddress).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(token));
	}

	@Test
	@DisplayName(value = "한개의 key값에 저장된 주소 값 여러개 중 하나의 주소 값에 대한 토큰을 삭제합니다.")
	void delete_token_one() {
		// given
		String email = "waterkite94@gmail.com";
		String address1 = "1";
		String address2 = "2";

		String token1 = "1234";
		String token2 = "5678";

		redisAuthTokenRepository.save(email, address1, token1);
		redisAuthTokenRepository.save(email, address2, token2);

		// when
		redisAuthTokenRepository.delete(email, address1);

		// then
		Optional<String> findTokenByEmailAndAddress
			= redisAuthTokenRepository.findTokenByEmailAndAddress(email, address1);

		assertThat(findTokenByEmailAndAddress).isEmpty();

		Optional<String> findTokenByEmailAndAddress2
			= redisAuthTokenRepository.findTokenByEmailAndAddress(email, address2);
		assertThat(findTokenByEmailAndAddress2).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(token2));
	}

	@Test
	@DisplayName(value = "key 값에 지정된 모든 토큰을 삭제합니다.")
	void delete_token_all() {
		// given
		String email = "waterkite94@gmail.com";
		String address1 = "1234";
		String address2 = "5678";

		String token1 = "1234";
		String token2 = "5678";

		redisAuthTokenRepository.save(email, address1, token1);
		redisAuthTokenRepository.save(email, address2, token2);

		// when
		redisAuthTokenRepository.deleteAll(email);

		// then
		Optional<String> findTokenByEmailAndAddress = redisAuthTokenRepository.findTokenByEmailAndAddress(email,
			token1);
		assertThat(findTokenByEmailAndAddress).isEmpty();

		Optional<String> findTokenByEmailAndAddress2 = redisAuthTokenRepository.findTokenByEmailAndAddress(email,
			token2);
		assertThat(findTokenByEmailAndAddress2).isEmpty();
	}
}
