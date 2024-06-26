package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;

import project.tosstock.member.application.domain.model.UpdateMemberDto;

public interface MemberRepositoryCustom {

	Optional<String> findPasswordByEmail(String email);

	Optional<String> findUsernameById(Long memberId);

	void updateInfo(Long memberId, UpdateMemberDto updateMemberDto);

	void updatePassword(String email, String password);
}
