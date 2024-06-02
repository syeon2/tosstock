package project.tosstock.member.application.port.out;

import project.tosstock.member.adapter.out.entity.MemberEntity;

public interface FindMemberPort {

	MemberEntity findMemberById(Long memberId);

	String findPasswordByEmail(String email);
}
