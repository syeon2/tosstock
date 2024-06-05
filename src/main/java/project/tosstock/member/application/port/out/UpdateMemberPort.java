package project.tosstock.member.application.port.out;

import project.tosstock.member.application.domain.model.UpdateMemberDto;

public interface UpdateMemberPort {

	void updateInfo(Long memberId, UpdateMemberDto updateMemberDto);

	void updatePassword(Long id, String password);
}
