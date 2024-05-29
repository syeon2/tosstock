package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeUsernameRequest {

	@NotBlank(message = "이름은 필수 값입니다.")
	private String username;

	public ChangeUsernameRequest(String username) {
		this.username = username;
	}
}
