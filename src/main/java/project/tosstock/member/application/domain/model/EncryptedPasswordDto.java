package project.tosstock.member.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EncryptedPasswordDto {

	private final String password;
	private final String salt;

	@Builder
	private EncryptedPasswordDto(String password, String salt) {
		this.password = password;
		this.salt = salt;
	}

}
