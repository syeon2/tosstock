package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.JwtTokenDto;

public interface AuthenticationMemberUseCase {

	JwtTokenDto login(String email, String password, String address);

	boolean logout(String email, String address);

	boolean logoutAll(String email);

	JwtTokenDto updateJwtToken(String refreshToken);
}
