package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.JwtTokenDto;

public interface AuthMemberUseCase {

	JwtTokenDto login(String email, String password, String address);

	void logout(String email, String address);

	void logoutAll(String email);

	JwtTokenDto updateJwtToken(String refreshToken);
}
