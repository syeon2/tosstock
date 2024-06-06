package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisAuthCodeRepository;
import project.tosstock.member.application.port.out.FindAuthCodePort;
import project.tosstock.member.application.port.out.SaveAuthCodePort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements SaveAuthCodePort, FindAuthCodePort {

	private final RedisAuthCodeRepository redisAuthCodeRepository;

	@Override
	public void save(String email, String code) {
		redisAuthCodeRepository.save(email, code);
	}

	@Override
	public Optional<String> findAuthCodeByMail(String email) {
		return redisAuthCodeRepository.findCodeByEmail(email);
	}
}
