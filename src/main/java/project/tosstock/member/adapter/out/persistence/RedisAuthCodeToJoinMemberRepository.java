package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisAuthCodeToJoinMemberRepository {

	@Value("${spring.data.redis_mail.expire_minutes}")
	private Integer expireMinutes;

	private final RedisTemplate<String, String> redisTemplate;

	public RedisAuthCodeToJoinMemberRepository(
		@Qualifier(value = "redisAuthCodeForMailTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void save(String email, String code) {
		redisTemplate.opsForValue().set(email, code, expireMinutes, TimeUnit.MINUTES);
	}

	public Optional<String> findCodeByEmail(String email) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(email));
	}
}
