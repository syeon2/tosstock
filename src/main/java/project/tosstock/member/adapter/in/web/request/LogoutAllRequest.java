package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutAllRequest {

	@Email(message = "아이디는 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 값입니다.")
	private String email;

	public LogoutAllRequest(String email) {
		this.email = email;
	}
}
