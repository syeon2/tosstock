package project.tosstock.common.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisAuthTokenConfig {

	@Value(value = "${spring.data.redis_token.host}")
	private String host;

	@Value(value = "${spring.data.redis_token.port}")
	private Integer port;

	@Bean
	@Qualifier(value = "redisAuthTokenConnectionFactory")
	public RedisConnectionFactory redisAuthTokenConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisAuthTokenTemplate")
	public RedisTemplate<String, Object> redisTemplate(
		@Qualifier(value = "redisAuthTokenConnectionFactory") RedisConnectionFactory redisAuthTokenConnectionFactory
	) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisAuthTokenConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}
}
