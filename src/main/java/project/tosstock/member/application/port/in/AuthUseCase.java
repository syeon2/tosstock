package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.JwtTokenDto;

public interface AuthUseCase {

	JwtTokenDto renewTokens(String email, String refreshToken);
}
