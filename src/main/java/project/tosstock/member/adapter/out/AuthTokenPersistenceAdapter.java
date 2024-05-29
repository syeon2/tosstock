package project.tosstock.member.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisAuthTokenRepository;
import project.tosstock.member.application.port.out.SaveTokenPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthTokenPersistenceAdapter implements SaveTokenPort {

	private final RedisAuthTokenRepository redisAuthTokenRepository;

	public void save(String email, String address, String token) {
		redisAuthTokenRepository.save(email, address, token);
	}
}
