package project.tosstock.member.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.member.application.domain.model.JwtTokenDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenResponse {

	private String accessToken;

	private String refreshToken;

	@Builder
	private JwtTokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JwtTokenResponse of(JwtTokenDto jwtTokenDto) {
		return JwtTokenResponse.builder()
			.accessToken(jwtTokenDto.getAccessToken())
			.refreshToken(jwtTokenDto.getRefreshToken())
			.build();
	}
}
