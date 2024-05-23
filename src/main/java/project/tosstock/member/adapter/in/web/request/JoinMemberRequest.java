package project.tosstock.member.adapter.in.web.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.member.application.domain.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinMemberRequest {

	@NotBlank(message = "이름은 필수 값입니다.")
	private String username;

	@Email(message = "이메일 형식으로 가입 가능합니다. (ex. xxx@xxx.com)")
	@NotBlank(message = "이메일은 필수값입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 값입니다.")
	@Length(min = 8, max = 20, message = "비밀번호는 8 ~ 20자리입니다.")
	private String password;

	@NotBlank(message = "전화번호는 필수 값입니다.")
	@Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11자리의 숫자만 입력 가능합니다.")
	private String phoneNumber;

	private String introduce;

	private String profileImageUrl;

	@NotBlank(message = "이메일 인증 번호는 필수 값입니다.")
	private String authCode;

	@Builder
	private JoinMemberRequest(String username, String email, String password, String phoneNumber, String introduce,
		String profileImageUrl, String authCode) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
		this.authCode = authCode;
	}

	public Member toDomain() {
		return Member.builder()
			.username(this.username)
			.email(this.email)
			.password(this.password)
			.phoneNumber(this.phoneNumber)
			.introduce(this.introduce)
			.profileImageUrl(this.profileImageUrl)
			.build();
	}
}
