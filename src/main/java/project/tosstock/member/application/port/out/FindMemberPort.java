package project.tosstock.member.application.port.out;

import java.util.Optional;

import project.tosstock.member.application.domain.model.Member;

public interface FindMemberPort {

	Optional<String> findUsernameById(Long memberId);

	Optional<String> findPasswordByEmail(String email);

	Optional<Member> findMemberByEmailOrPhoneNumber(String email, String phoneNumber);
}
