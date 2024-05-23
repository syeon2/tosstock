package project.tosstock.member.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.member.adapter.out.entity.EncryptedPassword;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.application.domain.model.EncryptedPasswordDto;
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

	public MemberEntity toEntity(Member domain, EncryptedPasswordDto passwordDto) {
		EncryptedPassword passwordEntity = toPasswordEntity(passwordDto);

		return MemberEntity.builder()
			.username(domain.getUsername())
			.encryptedPassword(passwordEntity)
			.email(domain.getEmail())
			.phoneNumber(domain.getPhoneNumber())
			.introduce(domain.getIntroduce())
			.profileImageUrl(domain.getProfileImageUrl())
			.build();
	}

	private EncryptedPassword toPasswordEntity(EncryptedPasswordDto passwordDto) {
		return EncryptedPassword.builder()
			.password(passwordDto.getPassword())
			.salt(passwordDto.getSalt())
			.build();
	}
}
