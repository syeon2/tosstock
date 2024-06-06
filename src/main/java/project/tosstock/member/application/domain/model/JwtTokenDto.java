package project.tosstock.member.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDto {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	private JwtTokenDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JwtTokenDto of(String accessToken, String refreshToken) {
		return JwtTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
