package project.tosstock.member.adapter.out.persistence;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.application.port.out.SaveTokenPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthTokenPersistenceAdapter implements SaveTokenPort {

	private final RedisAuthTokenRepository redisAuthTokenRepository;

	public void save(String email, String token, LocalDateTime expiredTime) {
		redisAuthTokenRepository.save(email, token, expiredTime);
	}
}
