package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;

public interface MemberRepositoryCustom {

	Optional<String> findPasswordByEmail(String email);
}
