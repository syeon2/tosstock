package project.tosstock.member.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisAuthCodeByMailRepository;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodeByMailPort {

	private final RedisAuthCodeByMailRepository redisAuthCodeByMailRepository;

	@Override
	public void save(String email, String code) {
		redisAuthCodeByMailRepository.save(email, code);
	}

	@Override
	public String findAuthCodeByMail(String email) {
		return redisAuthCodeByMailRepository.findCodeByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("인증번호가 존재하지 않습니다."));
	}
}
