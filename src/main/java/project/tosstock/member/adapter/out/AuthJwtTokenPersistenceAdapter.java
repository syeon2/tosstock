package project.tosstock.member.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisJwtTokenRepository;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.SaveTokenPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthJwtTokenPersistenceAdapter implements SaveTokenPort, DeleteJwtTokenPort {

	private final RedisJwtTokenRepository redisJwtTokenRepository;

	@Override
	public void save(String email, String address, String token) {
		redisJwtTokenRepository.save(email, address, token);
	}

	@Override
	public void mergeByEmailAndToken(String email, String prevToken, String renewToken) {
		redisJwtTokenRepository.mergeToken(email, prevToken, renewToken);
	}

	@Override
	public void deleteOne(String email, String address) {
		redisJwtTokenRepository.delete(email, address);
	}

	@Override
	public void deleteAll(String email) {
		redisJwtTokenRepository.deleteAll(email);
	}
}
