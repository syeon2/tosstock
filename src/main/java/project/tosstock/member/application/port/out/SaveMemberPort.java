package project.tosstock.member.application.port.out;

import project.tosstock.member.application.domain.model.EncryptedPasswordDto;
import project.tosstock.member.application.domain.model.Member;

public interface SaveMemberPort {

	Long saveMember(Member member, EncryptedPasswordDto passwordDto);
}
