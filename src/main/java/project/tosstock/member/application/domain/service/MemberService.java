package project.tosstock.member.application.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.DuplicateAccountException;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;
import project.tosstock.member.application.port.out.DeleteTokenPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;
import project.tosstock.member.application.port.out.ValidateMemberPort;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinMemberUseCase, UpdateMemberUseCase {

	private final SaveMemberPort saveMemberPort;
	private final ValidateMemberPort validateMemberPort;
	private final AuthCodeForMemberPort authCodeForMemberPort;
	private final UpdateMemberPort updateMemberPort;
	private final DeleteTokenPort deleteTokenPort;

	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long joinMember(Member member, String authCode) {
		checkDuplicatedMember(member);
		checkAuthCodeByEmail(member.getEmail(), authCode);

		String encodedPassword = passwordEncoder.encode(member.getPassword());

		return saveMemberPort.saveMember(member, encodedPassword);
	}

	@Override
	@Transactional
	public void updateUsername(Long id, String username) {
		updateMemberPort.updateUsername(id, username);
	}

	@Override
	@Transactional
	public void updateProfileImageUrl(Long id, String profileImageUrl) {
		updateMemberPort.updateProfileImageUrl(id, profileImageUrl);
	}

	@Override
	@Transactional
	public void updatePassword(Long id, String email, String password) {
		String encodedPassword = passwordEncoder.encode(password);

		deleteTokenPort.deleteAll(email);
		updateMemberPort.updatePassword(id, encodedPassword);
	}

	private void checkAuthCodeByEmail(String email, String code) {
		String storedCode = authCodeForMemberPort.findAuthCodeByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("인증번호가 존재하지 않습니다."));

		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
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
