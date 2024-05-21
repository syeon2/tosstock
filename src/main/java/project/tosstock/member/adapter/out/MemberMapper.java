package project.tosstock.member.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.member.adapter.out.entity.EncryptedPassword;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.application.domain.model.Member;

@Component
public class MemberMapper {

	public Member toDomain(MemberEntity entity) {
		return Member.builder()
			.id(entity.getId())
			.username(entity.getUsername())
			.email(entity.getEmail())
			.password(entity.getEncryptedPassword().getPassword())
			.phoneNumber(entity.getPhoneNumber())
			.introduce(entity.getIntroduce())
			.profileImageUrl(entity.getProfileImageUrl())
			.build();
	}

	public MemberEntity toEntity(Member domain) {
		return MemberEntity.builder()
			.username(domain.getUsername())
			.encryptedPassword(getEncryptedPassword(domain.getPassword()))
			.email(domain.getEmail())
			.phoneNumber(domain.getPhoneNumber())
			.introduce(domain.getIntroduce())
			.profileImageUrl(domain.getProfileImageUrl())
			.build();
	}

	// TODO:: Encrypted Password and Get Salt
	private EncryptedPassword getEncryptedPassword(String password) {
		return EncryptedPassword.builder()
			.password(password)
			.salt("salt")
			.build();
	}
}
