package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.out.LoginPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.ValidateMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements SaveMemberPort, ValidateMemberPort, LoginPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public Long saveMember(Member member, String encodedPassword) {
		MemberEntity entity = memberMapper.toEntity(member, encodedPassword);
		memberRepository.save(entity);

		return entity.getId();
	}

	@Override
	public boolean isDuplicatedEmail(String email) {
		return memberRepository.findByEmail(email).isPresent();
	}

	@Override
	public boolean isExistPhoneNumber(String phoneNumber) {
		return memberRepository.findByPhoneNumber(phoneNumber).isPresent();
	}

	@Override
	public Optional<String> findPasswordByEmail(String email) {
		return memberRepository.findPasswordByEmail(email);
	}
}
