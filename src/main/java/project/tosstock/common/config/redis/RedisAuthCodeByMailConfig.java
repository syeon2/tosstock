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
public class RedisAuthCodeByMailConfig {

	@Value("${spring.data.redis-mail.host}")
	private String host;

	@Value("${spring.data.redis-mail.port}")
	private Integer port;

	@Bean
	@Qualifier(value = "redisAuthCodeByMailConnectionFactory")
	public RedisConnectionFactory redisAuthCodeByMailConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisAuthCodeTemplate")
	public RedisTemplate<String, String> redisAuthCodeTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisAuthCodeByMailConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}
}
