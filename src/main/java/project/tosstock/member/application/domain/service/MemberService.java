package project.tosstock.member.application.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.DuplicatedAccountException;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;
import project.tosstock.member.application.port.out.VerifyMemberPort;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinMemberUseCase, UpdateMemberUseCase {

	private final SaveMemberPort saveMemberPort;
	private final VerifyMemberPort verifyMemberPort;
	private final UpdateMemberPort updateMemberPort;

	private final AuthCodeByMailPort authCodeByMailPort;
	private final DeleteJwtTokenPort deleteJwtTokenPort;

	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long joinMember(Member member, String authCode) {
		checkAuthCodeByMail(member.getEmail(), authCode);
		checkDuplicatedMember(member);

		return saveMemberPort.save(member, passwordEncoder.encode(member.getPassword()));
	}

	@Override
	@Transactional
	public boolean changeUsername(Long id, String username) {
		updateMemberPort.updateUsername(id, username);

		return true;
	}

	@Override
	@Transactional
	public boolean changeProfileImageUrl(Long id, String profileImageUrl) {
		updateMemberPort.updateProfileImageUrl(id, profileImageUrl);

		return true;
	}

	@Override
	@Transactional
	public boolean changePassword(Long id, String email, String password) {
		String encodedPassword = passwordEncoder.encode(password);

		deleteJwtTokenPort.deleteAll(email);
		updateMemberPort.updatePassword(id, encodedPassword);

		return true;
	}

	private void checkAuthCodeByMail(String email, String code) {
		String storedCode = authCodeByMailPort.findAuthCodeByMail(email)
			.orElseThrow(() -> new IllegalArgumentException("인증번호가 존재하지 않습니다."));

		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
	}

	private void checkDuplicatedMember(Member member) {
		if (verifyMemberPort.isDuplicatedEmail(member.getEmail())) {
			throw new DuplicatedAccountException("이미 존재하는 이메일입니다.");
		}

		if (verifyMemberPort.isExistPhoneNumber(member.getPhoneNumber())) {
			throw new DuplicatedAccountException("이미 가입된 전화번호입니다.");
		}
	}
}
