package project.tosstock.member.adapter.out;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.out.LoginPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;
import project.tosstock.member.application.port.out.VerifyMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements SaveMemberPort, VerifyMemberPort, UpdateMemberPort, LoginPort {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public Long save(Member member, String encodedPassword) {
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
	public String findPasswordByEmail(String email) {
		return memberRepository.findPasswordByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
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
