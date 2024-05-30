package project.tosstock.member.application.domain.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.jwt.JwtTokenProvider;
import project.tosstock.common.jwt.TokenType;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthUseCase;
import project.tosstock.member.application.port.in.LoginUseCase;
import project.tosstock.member.application.port.out.DeleteTokenPort;
import project.tosstock.member.application.port.out.LoginPort;
import project.tosstock.member.application.port.out.SaveTokenPort;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, AuthUseCase {

	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	private final LoginPort loginPort;
	private final SaveTokenPort saveTokenPort;
	private final DeleteTokenPort deleteTokenPort;

	@Override
	@Transactional(readOnly = true)
	public JwtTokenDto login(String email, String password, String address) {
		boolean result = passwordEncoder.matches(password, findPasswordByEmail(email));

		if (!result) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}

		JwtTokenDto jwtTokenDto = createJwtTokenDto(email);
		saveTokenPort.save(email, address, jwtTokenDto.getRefreshToken());

		return jwtTokenDto;
	}

	@Override
	public void logout(String email, String address) {
		deleteTokenPort.deleteOne(email, address);
	}

	@Override
	public void logoutAll(String email) {
		deleteTokenPort.deleteAll(email);
	}

	@Override
	public JwtTokenDto renewTokens(String email, String refreshToken) {
		try {
			jwtTokenProvider.verifyToken(refreshToken);
			JwtTokenDto jwtTokenDto = createJwtTokenDto(email, refreshToken);
			saveTokenPort.mergeByEmailAndToken(email, refreshToken, jwtTokenDto.getRefreshToken());

			return jwtTokenDto;
		} catch (ExpiredJwtException exception) {
			JwtTokenDto jwtTokenDto = createJwtTokenDto(email);
			saveTokenPort.mergeByEmailAndToken(email, refreshToken, jwtTokenDto.getRefreshToken());

			return jwtTokenDto;
		} catch (RuntimeException exception) {
			throw new IllegalArgumentException("잘못된 토큰입니다.");
		}
	}

	private JwtTokenDto createJwtTokenDto(String email) {
		String accessToken = jwtTokenProvider.createToken(email, TokenType.ACCESS_TOKEN);
		String refreshToken = jwtTokenProvider.createToken(email, TokenType.REFRESH_TOKEN);

		return JwtTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private JwtTokenDto createJwtTokenDto(String email, String refreshToken) {
		String accessToken = jwtTokenProvider.createToken(email, TokenType.ACCESS_TOKEN);

		return JwtTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private String findPasswordByEmail(String email) {
		Optional<String> findPasswordOptional = loginPort.findPasswordByEmail(email);

		if (findPasswordOptional.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
		}
		return findPasswordOptional.get();
	}
}
