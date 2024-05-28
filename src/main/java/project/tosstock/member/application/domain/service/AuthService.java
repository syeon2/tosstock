package project.tosstock.member.application.domain.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.LoginUseCase;
import project.tosstock.member.application.port.out.LoginPort;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase {

	private final PasswordEncoder passwordEncoder;

	private final LoginPort loginPort;

	@Override
	@Transactional(readOnly = true)
	public JwtTokenDto login(String email, String password) {
		boolean result = passwordEncoder.matches(password, findPasswordByEmail(email));

		if (result) {
			// TODO:: JWT Token 생성

			return JwtTokenDto.builder()
				.accessToken("accessToken")
				.refreshToken("refreshToken")
				.build();
		}

		throw new IllegalArgumentException("잘못된 비밀번호입니다.");
	}

	private String findPasswordByEmail(String email) {
		Optional<String> findPasswordOptional = loginPort.findPasswordByEmail(email);

		if (findPasswordOptional.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
		}
		return findPasswordOptional.get();
	}
}
