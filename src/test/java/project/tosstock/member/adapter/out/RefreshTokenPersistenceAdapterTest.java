package project.tosstock.member.adapter.out;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import project.tosstock.IntegrationTestSupport;

class RefreshTokenPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private RefreshTokenPersistenceAdapter refreshTokenPersistenceAdapter;

	@Autowired
	@Qualifier(value = "redisRefreshTokenTemplate")
	private RedisTemplate<String, Object> redisRefreshTokenTemplate;

	@BeforeEach
	void before() {
		redisRefreshTokenTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@Test
	@DisplayName(value = "토큰을 저장합니다.")
	void save() {
		// given
		String email = "waterkite94@gmail.com";
		String address = "1";
		String token = "asdfalsdkfasldfj";

		// when
		refreshTokenPersistenceAdapter.save(email, address, token);

		// then
		Object findToken = redisRefreshTokenTemplate.opsForHash().get(email, address);

		assertThat(findToken).isNotNull();
		assertThat((String)findToken).isEqualTo(token);
	}

	@Test
	@DisplayName(value = "한개의 key값에 저장된 주소 값 여러개 중 하나의 주소 값에 대한 토큰을 삭제합니다.")
	void deleteToken_one() {
		// given
		String email = "waterkite94@gmail.com";
		String address1 = "1";
		String address2 = "2";

		String token1 = "1234";
		String token2 = "5678";

		redisRefreshTokenTemplate.opsForHash().put(email, address1, token1);
		redisRefreshTokenTemplate.opsForHash().put(email, address2, token2);

		// when
		refreshTokenPersistenceAdapter.delete(email, address1);

		// then
		Object findToken1 = redisRefreshTokenTemplate.opsForHash().get(email, address1);
		assertThat(findToken1).isNull();

		Object findToken2 = redisRefreshTokenTemplate.opsForHash().get(email, address2);
		assertThat((String)findToken2).isNotNull().isEqualTo(token2);
	}

	@Test
	@DisplayName(value = "key 값에 지정된 모든 토큰을 삭제합니다.")
	void deleteToken_all() {
		// given
		String email = "waterkite94@gmail.com";
		String address1 = "1234";
		String address2 = "5678";

		String token1 = "1234";
		String token2 = "5678";

		redisRefreshTokenTemplate.opsForHash().put(email, address1, token1);
		redisRefreshTokenTemplate.opsForHash().put(email, address2, token2);

		// when
		refreshTokenPersistenceAdapter.deleteAll(email);

		// then
		Object findToken1 = redisRefreshTokenTemplate.opsForHash().get(email, address1);
		assertThat(findToken1).isNull();

		Object findToken2 = redisRefreshTokenTemplate.opsForHash().get(email, address2);
		assertThat(findToken2).isNull();
	}
}
