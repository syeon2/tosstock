package project.tosstock.member.adapter.out.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	public void save(String email, String token, LocalDateTime time) {
		String format = convertDateTimeToString(time);

		Boolean result = redisTemplate.opsForHash().putIfAbsent(email, token, format);

		if (!result) {
			redisTemplate.opsForHash().put(email, token, format);
		}
	}

	public Optional<LocalDateTime> findTimeByIdAndToken(String email, String token) {
		String expiredTime = (String)redisTemplate.opsForHash().get(email, token);

		if (expiredTime == null) {
			return Optional.empty();
		}

		LocalDateTime convertStringToLocalDateTime = convertStringToLocalDateTime(expiredTime);
		return Optional.of(convertStringToLocalDateTime);
	}

	public void delete(String email, String token) {
		redisTemplate.opsForHash().delete(email, token);
	}

	public void deleteAll(String email) {
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(email);

		for (Object hashKey : entries.keySet()) {
			redisTemplate.opsForHash().delete(email, hashKey);
		}
	}

	private LocalDateTime convertStringToLocalDateTime(String time) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(time, dateFormat);
	}

	private String convertDateTimeToString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return time.format(formatter);
	}
}
