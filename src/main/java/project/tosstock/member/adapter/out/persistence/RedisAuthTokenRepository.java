package project.tosstock.member.adapter.out.persistence;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisAuthTokenRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisAuthTokenRepository(
		@Qualifier(value = "redisAuthTokenTemplate") RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void save(String email, String address, String token) {
		Boolean result = redisTemplate.opsForHash().putIfAbsent(email, address, token);

		if (!result) {
			redisTemplate.opsForHash().put(email, address, token);
		}
	}

	public Optional<String> findTokenByEmailAndAddress(String email, String address) {
		String token = (String)redisTemplate.opsForHash().get(email, address);

		return Optional.ofNullable(token);
	}

	public void mergeToken(String email, String prevToken, String renewToken) {
		Map<Object, Object> hashKeys = redisTemplate.opsForHash().entries(email);

		for (Object hashKey : hashKeys.keySet()) {
			String address = (String)hashKey;
			String token = (String)redisTemplate.opsForHash().get(email, hashKey);

			if (token.equals(prevToken)) {
				redisTemplate.opsForHash().delete(email, address);
				redisTemplate.opsForHash().put(email, address, renewToken);

				return;
			}
		}
	}

	public void delete(String email, String address) {
		redisTemplate.opsForHash().delete(email, address);
	}

	public void deleteAll(String email) {
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(email);

		for (Object address : entries.keySet()) {
			redisTemplate.opsForHash().delete(email, address);
		}
	}
}
