package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisAuthCodeByMailRepository {

	@Value("${spring.data.redis-mail.expiration-minutes}")
	private Integer expirationMinutes;

	private final RedisTemplate<String, String> redisTemplate;

	public RedisAuthCodeByMailRepository(
		@Qualifier(value = "redisAuthCodeByMailTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void save(String email, String code) {
		redisTemplate.opsForValue().set(email, code, expirationMinutes, TimeUnit.MINUTES);
	}

	public Optional<String> findCodeByEmail(String email) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(email));
	}
}
