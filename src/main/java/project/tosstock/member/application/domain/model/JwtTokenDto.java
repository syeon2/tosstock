package project.tosstock.member.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDto {

	private String accessToken;
	private String refreshToken;

	@Builder
	private JwtTokenDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
