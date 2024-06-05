package project.tosstock.member.adapter.out;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
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
	public MemberEntity findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}

	@Override
	public String findPasswordByEmail(String email) {
		return memberRepository.findPasswordByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
	}

	@Override
	public Optional<MemberEntity> findMemberByEmailOrPhoneNumber(String email, String phoneNumber) {
		return memberRepository.findByEmailOrPhoneNumber(email, phoneNumber);
	}

	@Override
	@Transactional
	public void updateUsername(Long id, String username) {
		memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."))
			.changeUsername(username);
	}

	@Override
	@Transactional
	public void updateProfileImageUrl(Long id, String profileImageUrl) {
		memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."))
			.changeProfileImageUrl(profileImageUrl);
	}

	@Override
	@Transactional
	public void updatePassword(Long id, String password) {
		memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."))
			.changePassword(password);
	}
}
