package project.tosstock.member.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.DuplicateAccountException;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.ValidateMemberPort;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinMemberUseCase {

	private final SaveMemberPort saveMemberPort;
	private final ValidateMemberPort validateMemberPort;

	@Override
	@Transactional
	public Long joinMember(Member member) {
		if (validateMemberPort.isDuplicatedEmail(member.getEmail())) {
			throw new DuplicateAccountException("이미 존재하는 이메일입니다.");
		}

		if (validateMemberPort.isExistPhoneNumber(member.getPhoneNumber())) {
			throw new DuplicateAccountException("이미 가입된 전화번호입니다.");
		}

		return saveMemberPort.saveMember(member);
	}

}
