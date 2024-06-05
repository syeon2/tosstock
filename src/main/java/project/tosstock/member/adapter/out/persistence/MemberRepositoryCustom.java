package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;

import project.tosstock.member.application.domain.model.UpdateMemberDto;

public interface MemberRepositoryCustom {

	Optional<String> findPasswordByEmail(String email);

	void updateInfo(Long memberId, UpdateMemberDto updateMemberDto);

	void updatePassword(Long memberId, String password);
}
