package project.tosstock.member.adapter.out;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.out.LoginPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;
import project.tosstock.member.application.port.out.ValidateMemberPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements SaveMemberPort, ValidateMemberPort, LoginPort, UpdateMemberPort {

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

	@Override
	@Transactional
	public void updateUsername(Long id, String username) {
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(id);

		findMemberOptional.ifPresent(m -> m.changeUsername(username));
		if (findMemberOptional.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
		}
	}

	@Override
	@Transactional
	public void updateProfileImageUrl(Long id, String profileImageUrl) {
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(id);

		findMemberOptional.ifPresent(m -> m.changeProfileImageUrl(profileImageUrl));
		if (findMemberOptional.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
		}
	}

	@Override
	@Transactional
	public void updatePassword(Long id, String password) {
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(id);

		findMemberOptional.ifPresent(m -> m.changePassword(password));
		if (findMemberOptional.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
		}
	}
}
