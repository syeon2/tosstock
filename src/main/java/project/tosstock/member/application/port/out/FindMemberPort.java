package project.tosstock.member.application.port.out;

import java.util.Optional;

import project.tosstock.member.adapter.out.entity.MemberEntity;

public interface FindMemberPort {

	MemberEntity findMemberById(Long memberId);

	String findPasswordByEmail(String email);

	Optional<MemberEntity> findMemberByEmailOrPhoneNumber(String email, String phoneNumber);
}
