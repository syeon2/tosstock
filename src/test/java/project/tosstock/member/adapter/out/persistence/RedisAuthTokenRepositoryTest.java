package project.tosstock.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;

class RedisAuthTokenRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private RedisAuthTokenRepository redisAuthTokenRepository;

	@Test
	@DisplayName(value = "Id와 token, expired time을 redis hash 구조로 저장합니다.")
	void save() {
		// given
		String email = "waterkite94@gmail.com";
		String token = "token";
		LocalDateTime time = LocalDateTime.now();
		String convertTime = convertDateTimeToString(time);

		// when
		redisAuthTokenRepository.save(email, token, time);

		// then
		Optional<LocalDateTime> findTimeByIdAndTokenOptional
			= redisAuthTokenRepository.findTimeByIdAndToken(email, token);

		assertThat(findTimeByIdAndTokenOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(convertDateTimeToString(s)).isEqualTo(convertTime));
	}

	@Test
	@DisplayName(value = "한개의 key값에 저장된 토큰 값 여러개 중 지정된 토큰 한개만 삭제합니다.")
	void delete_token_one() {
		// given
		String email = "waterkite94@gmail.com";
		String token1 = "token1";
		String token2 = "token2";

		LocalDateTime time = LocalDateTime.now();

		redisAuthTokenRepository.save(email, token1, time);
		redisAuthTokenRepository.save(email, token2, time);

		// when
		redisAuthTokenRepository.delete(email, token1);

		// then
		Optional<LocalDateTime> findTimeByIdAndToken = redisAuthTokenRepository.findTimeByIdAndToken(email, token1);

		assertThat(findTimeByIdAndToken).isEmpty();

		Optional<LocalDateTime> findTimeByIdAndToken2 = redisAuthTokenRepository.findTimeByIdAndToken(email, token2);
		assertThat(findTimeByIdAndToken2).isPresent()
			.hasValueSatisfying(s -> assertThat(convertDateTimeToString(s)).isEqualTo(convertDateTimeToString(time)));
	}

	@Test
	@DisplayName(value = "key 값에 지정된 모든 토큰을 삭제합니다.")
	void delete_token_all() {
		// given
		String email = "waterkite94@gmail.com";
		String token1 = "token1";
		String token2 = "token2";

		LocalDateTime time = LocalDateTime.now();

		redisAuthTokenRepository.save(email, token1, time);
		redisAuthTokenRepository.save(email, token2, time);

		// when
		redisAuthTokenRepository.deleteAll(email);

		// then
		Optional<LocalDateTime> findTimeByIdAndToken1 = redisAuthTokenRepository.findTimeByIdAndToken(email, token1);
		assertThat(findTimeByIdAndToken1).isEmpty();

		Optional<LocalDateTime> findTimeByIdAndToken2 = redisAuthTokenRepository.findTimeByIdAndToken(email, token2);
		assertThat(findTimeByIdAndToken2).isEmpty();
	}

	private String convertDateTimeToString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return time.format(formatter);
	}
}
