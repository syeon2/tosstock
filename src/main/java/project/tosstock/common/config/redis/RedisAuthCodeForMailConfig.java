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
public class RedisAuthCodeForMailConfig {

	@Value("${spring.data.redis_mail.host}")
	private String host;

	@Value("${spring.data.redis_mail.port}")
	private Integer port;

	@Bean
	@Qualifier(value = "redisAuthCodeForMailConnectionFactory")
	public RedisConnectionFactory redisAuthCodeForMailConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisAuthCodeForMailTemplate")
	public RedisTemplate<String, String> redisAuthCodeForMailTemplate(
		@Qualifier(value = "redisAuthCodeForMailConnectionFactory")
		RedisConnectionFactory redisAuthCodeForMailConnectionFactory
	) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisAuthCodeForMailConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}
}
