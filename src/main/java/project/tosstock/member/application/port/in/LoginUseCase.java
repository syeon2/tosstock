package project.tosstock.member.application.port.in;

import project.tosstock.member.application.domain.model.JwtTokenDto;

public interface LoginUseCase {

	JwtTokenDto login(String email, String password, String address);
}
