package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequest {

	@NotBlank(message = "비밀번호는 필수 값입니다.")
	private String password;

	@Email(message = "아이디는 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 값입니다.")
	private String email;

	@Builder
	private ChangePasswordRequest(String password, String email) {
		this.password = password;
		this.email = email;
	}
}
