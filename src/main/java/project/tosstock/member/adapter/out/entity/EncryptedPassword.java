package project.tosstock.member.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedPassword {

	@Column(name = "password", columnDefinition = "char(64)", nullable = false)
	private String password;

	@Column(name = "salt", columnDefinition = "char(40)", nullable = false)
	private String salt;

	@Builder
	private EncryptedPassword(String password, String salt) {
		this.password = password;
		this.salt = salt;
	}
}
