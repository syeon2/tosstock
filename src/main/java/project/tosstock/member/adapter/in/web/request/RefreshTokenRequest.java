package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenRequest {

	@NotBlank(message = "Refresh Token은 필수 값입니다.")
	private String refreshToken;

	public RefreshTokenRequest(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
