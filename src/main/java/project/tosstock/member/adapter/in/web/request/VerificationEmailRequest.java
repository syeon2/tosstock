package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationEmailRequest {

	@Email(message = "이메일 형식으로 요청 가능합니다. (ex. xxx@xxx.com)")
	@NotBlank(message = "인증 요청을 위한 이메일 값은 필수입니다.")
	private String email;

	public VerificationEmailRequest(String email) {
		this.email = email;
	}
}
