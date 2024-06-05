package project.tosstock.member.application.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.jwt.JwtPayloadDto;
import project.tosstock.common.jwt.JwtTokenProvider;
import project.tosstock.common.jwt.TokenType;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthMemberUseCase;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.member.application.port.out.SaveJwtTokenPort;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthMemberUseCase {

	private final FindMemberPort findMemberPort;
	private final SaveJwtTokenPort saveJwtTokenPort;
	private final DeleteJwtTokenPort deleteJwtTokenPort;

	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	@Transactional(readOnly = true)
	public JwtTokenDto login(String email, String password, String address) {
		isMatchPassword(email, password);

		JwtTokenDto jwtTokenDto = createJwtTokenDto(email, address);
		saveJwtTokenPort.save(email, address, jwtTokenDto.getRefreshToken());

		return jwtTokenDto;
	}

	@Override
	public void logout(String email, String address) {
		deleteJwtTokenPort.delete(email, address);
	}

	@Override
	public void logoutAll(String email) {
		deleteJwtTokenPort.deleteAll(email);
	}

	@Override
	public JwtTokenDto updateJwtToken(String refreshToken) {
		checkWrongRefreshToken(refreshToken);

		return renewAndSaveJwtTokens(refreshToken);
	}

	private boolean checkWrongRefreshToken(String refreshToken) {
		try {
			jwtTokenProvider.verifyToken(refreshToken);

			return false;
		} catch (ExpiredJwtException exception) {
			return false;
		} catch (RuntimeException exception) {
			throw new IllegalArgumentException("잘못된 토큰입니다.");
		}
	}

	private JwtTokenDto renewAndSaveJwtTokens(String refreshToken) {
		JwtPayloadDto jwtPayload = jwtTokenProvider.getJwtPayload(refreshToken);
		JwtTokenDto jwtTokenDto = createJwtTokenDto(jwtPayload.getEmail(), jwtPayload.getAddress());

		saveJwtTokenPort.save(jwtPayload.getEmail(), jwtPayload.getAddress(), jwtTokenDto.getRefreshToken());

		return jwtTokenDto;
	}

	private void isMatchPassword(String email, String password) {
		boolean result = passwordEncoder.matches(password, findPasswordByEmail(email));

		if (!result) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
	}

	private JwtTokenDto createJwtTokenDto(String email, String address) {
		String accessToken = jwtTokenProvider.createToken(email, address, TokenType.ACCESS_TOKEN);
		String refreshToken = jwtTokenProvider.createToken(email, address, TokenType.REFRESH_TOKEN);

		return JwtTokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private String findPasswordByEmail(String email) {
		return findMemberPort.findPasswordByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}
}
