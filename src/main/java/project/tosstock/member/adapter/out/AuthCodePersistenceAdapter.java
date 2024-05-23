package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.persistence.TempAuthCodeForMemberRepository;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodeForMemberPort {

	private final TempAuthCodeForMemberRepository tempAuthCodeForMemberRepository;

	@Override
	public void saveAuthCode(String email, String code) {
		tempAuthCodeForMemberRepository.save(email, code);
	}

	@Override
	public Optional<String> findAuthCodeByEmail(String email) {
		return tempAuthCodeForMemberRepository.findCodeByEmail(email);
	}
}
