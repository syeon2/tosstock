package project.tosstock.member.application.port.out;

import project.tosstock.member.application.domain.model.Member;

public interface SaveMemberPort {

	Long saveMember(Member member);
}
