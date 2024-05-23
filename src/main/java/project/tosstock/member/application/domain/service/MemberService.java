package project.tosstock.member.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.encryption.Encryption;
import project.tosstock.common.encryption.EncryptionType;
import project.tosstock.common.error.exception.DuplicateAccountException;
import project.tosstock.member.application.domain.model.EncryptedPasswordDto;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.ValidateMemberPort;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinMemberUseCase {

	private final SaveMemberPort saveMemberPort;
	private final ValidateMemberPort validateMemberPort;
	private final AuthCodeForMemberPort authCodeForMemberPort;

	private static final EncryptionType ENCRYPTION_TYPE = EncryptionType.SHA_256;

	@Override
	@Transactional
	public Long joinMember(Member member, String authCode) {
		checkDuplicatedMember(member);
		checkAuthCodeByEmail(member.getEmail(), authCode);

		EncryptedPasswordDto passwordDto = createEncryptedPasswordDto(member.getPassword());

		return saveMemberPort.saveMember(member, passwordDto);
	}

	private void checkAuthCodeByEmail(String email, String code) {
		String storedCode = authCodeForMemberPort.findAuthCodeByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("인증번호가 존재하지 않습니다."));

		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
	}

	private EncryptedPasswordDto createEncryptedPasswordDto(String password) {
		String salt = Encryption.generateSalt();
		String saltedPassword = Encryption.createEncryptedSourceBySalt(password, salt, ENCRYPTION_TYPE);

		return EncryptedPasswordDto.builder()
			.password(saltedPassword)
			.salt(salt)
			.build();
	}

	private void checkDuplicatedMember(Member member) {
		if (validateMemberPort.isDuplicatedEmail(member.getEmail())) {
			throw new DuplicateAccountException("이미 존재하는 이메일입니다.");
		}

		if (validateMemberPort.isExistPhoneNumber(member.getPhoneNumber())) {
			throw new DuplicateAccountException("이미 가입된 전화번호입니다.");
		}
	}

}
