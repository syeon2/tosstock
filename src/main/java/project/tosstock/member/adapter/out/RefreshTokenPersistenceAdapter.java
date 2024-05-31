package project.tosstock.member.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisRefreshTokenRepository;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.SaveJwtTokenPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements SaveJwtTokenPort, DeleteJwtTokenPort {

	private final RedisRefreshTokenRepository redisRefreshTokenRepository;

	@Override
	public void save(String email, String address, String token) {
		redisRefreshTokenRepository.save(email, address, token);
	}

	@Override
	public void delete(String email, String address) {
		redisRefreshTokenRepository.delete(email, address);
	}

	@Override
	public void deleteAll(String email) {
		redisRefreshTokenRepository.deleteAll(email);
	}
}
