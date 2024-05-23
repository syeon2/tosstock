package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.Member;

public interface JoinMemberUseCase {

	Long joinMember(Member member, String authCode);
}
