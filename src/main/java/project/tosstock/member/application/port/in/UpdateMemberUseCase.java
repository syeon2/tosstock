package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.UpdateMemberDto;

public interface UpdateMemberUseCase {

	boolean changeMemberInfo(Long memberId, UpdateMemberDto updateMemberDto);

	boolean changePassword(String email, String password);
}
