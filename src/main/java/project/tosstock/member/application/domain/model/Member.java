package project.tosstock.member.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {

	private Long id;

	private String username;

	private String email;

	private String password;

	private String phoneNumber;

	private String introduce;

	private String profileImageUrl;

	@Builder
	private Member(Long id, String username, String email, String password, String phoneNumber, String introduce,
		String profileImageUrl) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}

	public void encryptePassword(String encodedPassword) {
		this.password = encodedPassword;
	}
}
