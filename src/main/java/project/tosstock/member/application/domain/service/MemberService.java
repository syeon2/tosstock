package project.tosstock.member.application.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.DuplicatedAccountException;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.domain.model.UpdateMemberDto;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinMemberUseCase, UpdateMemberUseCase {

	private final SaveMemberPort saveMemberPort;
	private final FindMemberPort findMemberPort;
	private final UpdateMemberPort updateMemberPort;

	private final AuthCodeByMailPort authCodeByMailPort;
	private final DeleteJwtTokenPort deleteJwtTokenPort;

	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long joinMember(Member member, String authCode) {
		checkAuthCodeByMail(member.getEmail(), authCode);
		checkDuplicatedMember(member);

		member.updateEncryptedPassword(encryptPassword(member.getPassword()));

		return saveMemberPort.save(member);
	}

	@Override
	@Transactional
	public boolean changeMemberInfo(Long memberId, UpdateMemberDto updateMemberDto) {
		updateMemberPort.updateInfo(memberId, updateMemberDto);

		return true;
	}

	@Override
	@Transactional
	public boolean changePassword(Long id, String email, String password) {
		deleteJwtTokenPort.deleteAll(email);

		updateMemberPort.updatePassword(id, encryptPassword(password));

		return true;
	}

	private void checkAuthCodeByMail(String email, String code) {
		String storedCode = authCodeByMailPort.findAuthCodeByMail(email);

		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
	}

	private void checkDuplicatedMember(Member member) {
		findMemberPort.findMemberByEmailOrPhoneNumber(member.getEmail(), member.getPhoneNumber())
			.ifPresent(m -> {
				throw new DuplicatedAccountException("이미 가입된 회원입니다.");
			});
	}

	private String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}
}
