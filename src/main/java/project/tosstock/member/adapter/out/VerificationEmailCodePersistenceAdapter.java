package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.RedisVerificationEmailCodeRepository;
import project.tosstock.member.application.port.out.FindVerificationEmailCodePort;
import project.tosstock.member.application.port.out.SaveVerificationEmailCodePort;

@PersistenceAdapter
@RequiredArgsConstructor
public class VerificationEmailCodePersistenceAdapter
	implements SaveVerificationEmailCodePort, FindVerificationEmailCodePort {

	private final RedisVerificationEmailCodeRepository redisVerificationEmailCodeRepository;

	@Override
	public void save(String email, String code) {
		redisVerificationEmailCodeRepository.save(email, code);
	}

	@Override
	public Optional<String> findAuthCodeByMail(String email) {
		return redisVerificationEmailCodeRepository.findCodeByEmail(email);
	}
}
