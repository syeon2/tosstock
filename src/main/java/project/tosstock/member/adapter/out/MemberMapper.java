package project.tosstock.member.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.application.domain.model.Member;

@Component
public class MemberMapper {

	public Member toDomain(MemberEntity entity) {
		return Member.builder()
			.id(entity.getId())
			.username(entity.getUsername())
			.email(entity.getEmail())
			.password(entity.getPassword())
			.phoneNumber(entity.getPhoneNumber())
			.introduce(entity.getIntroduce())
			.profileImageUrl(entity.getProfileImageUrl())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}

	public MemberEntity toEntity(Member domain) {
		return MemberEntity.builder()
			.username(domain.getUsername())
			.password(domain.getPassword())
			.email(domain.getEmail())
			.phoneNumber(domain.getPhoneNumber())
			.introduce(domain.getIntroduce())
			.profileImageUrl(domain.getProfileImageUrl())
			.build();
	}
}
