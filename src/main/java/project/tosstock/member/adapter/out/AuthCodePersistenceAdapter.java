package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisAuthCodeByMailRepository;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodeByMailPort {

	private final RedisAuthCodeByMailRepository redisAuthCodeByMailRepository;

	@Override
	public void saveAuthCode(String email, String code) {
		redisAuthCodeByMailRepository.save(email, code);
	}

	@Override
	public Optional<String> findAuthCodeByMail(String email) {
		return redisAuthCodeByMailRepository.findCodeByEmail(email);
	}
}
