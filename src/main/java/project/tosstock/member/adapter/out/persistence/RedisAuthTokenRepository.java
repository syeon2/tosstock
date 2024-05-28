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

	public void save(Long id, String token, LocalDateTime time) {
		String format = convertDateTimeToString(time);

		Boolean result = redisTemplate.opsForHash().putIfAbsent(id.toString(), token, format);

		if (!result) {
			redisTemplate.opsForHash().put(id.toString(), token, format);
		}
	}

	public Optional<LocalDateTime> findTimeByIdAndToken(Long id, String token) {
		String expiredTime = (String)redisTemplate.opsForHash().get(id.toString(), token);

		if (expiredTime == null) {
			return Optional.empty();
		}

		LocalDateTime convertStringToLocalDateTime = convertStringToLocalDateTime(expiredTime);
		return Optional.of(convertStringToLocalDateTime);
	}

	public void delete(Long id, String token) {
		redisTemplate.opsForHash().delete(id.toString(), token);
	}

	public void deleteAll(Long id) {
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(id.toString());

		for (Object hashKey : entries.keySet()) {
			redisTemplate.opsForHash().delete(id.toString(), hashKey);
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
