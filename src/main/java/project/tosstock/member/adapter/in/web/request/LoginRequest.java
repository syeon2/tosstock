package project.tosstock.member.adapter.in.web.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

	@Email(message = "아이디는 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 값입니다.")
	private String email;

	@Length(min = 8, max = 20, message = "비밀번호는 8 ~ 20 자리입니다.")
	@NotBlank(message = "비밀번호는 필수 값입니다.")
	private String password;

	@NotBlank(message = "기기 주소는 필수 값입니다.")
	private String address;

	@Builder
	private LoginRequest(String email, String password, String address) {
		this.email = email;
		this.password = password;
		this.address = address;
	}
}
