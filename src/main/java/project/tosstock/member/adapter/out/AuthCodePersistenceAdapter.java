package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisAuthCodeByMailRepository;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodeForMemberPort {

	private final RedisAuthCodeByMailRepository redisAuthCodeByMailRepository;

	@Override
	public void saveAuthCode(String email, String code) {
		redisAuthCodeByMailRepository.save(email, code);
	}

	@Override
	public Optional<String> findAuthCodeByEmail(String email) {
		return redisAuthCodeByMailRepository.findCodeByEmail(email);
	}
}
