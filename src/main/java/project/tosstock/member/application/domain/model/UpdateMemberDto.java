package project.tosstock.member.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberDto {

	private String username;
	private String introduce;
	private String profileImageUrl;

	@Builder
	private UpdateMemberDto(String username, String introduce, String profileImageUrl) {
		this.username = username;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}
}
