package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.UpdateMemberDto;

public interface UpdateMemberUseCase {

	boolean changeMemberInfo(Long memberId, UpdateMemberDto updateMemberDto);

	boolean changePassword(Long id, String email, String password);
}
