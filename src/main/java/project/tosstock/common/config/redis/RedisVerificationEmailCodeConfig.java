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
public class RedisVerificationEmailCodeConfig {

	@Value("${spring.data.redis-mail.host}")
	private String host;

	@Value("${spring.data.redis-mail.port}")
	private Integer port;

	@Bean
	@Qualifier(value = "redisVerificationEmailCodeConnectionFactory")
	public RedisConnectionFactory redisVerificationEmailCodeConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisVerificationEmailCodeTemplate")
	public RedisTemplate<String, String> redisVerificationEmailCodeTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisVerificationEmailCodeConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}
}
