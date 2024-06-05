package project.tosstock.member.adapter.out;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.domain.model.UpdateMemberDto;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements SaveMemberPort, UpdateMemberPort, FindMemberPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public Long save(Member member) {
		MemberEntity savedMemberEntity = memberRepository.save(memberMapper.toEntity(member));

		return savedMemberEntity.getId();
	}

	@Override
	public Optional<Member> findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.map(memberMapper::toDomain);
	}

	@Override
	public Optional<String> findPasswordByEmail(String email) {
		return memberRepository.findPasswordByEmail(email);
	}

	@Override
	public Optional<Member> findMemberByEmailOrPhoneNumber(String email, String phoneNumber) {
		return memberRepository.findByEmailOrPhoneNumber(email, phoneNumber)
			.map(memberMapper::toDomain);
	}

	@Override
	public void updateInfo(Long memberId, UpdateMemberDto updateMemberDto) {
		memberRepository.updateInfo(memberId, updateMemberDto);
	}

	@Override
	public void updatePassword(String email, String password) {
		memberRepository.updatePassword(email, password);
	}
}
