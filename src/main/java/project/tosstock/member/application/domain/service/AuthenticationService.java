package project.tosstock.member.application.domain.service;

import static project.tosstock.common.jwt.TokenType.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.jwt.JwtPayloadDto;
import project.tosstock.common.jwt.JwtTokenProvider;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthenticationMemberUseCase;
import project.tosstock.member.application.port.out.DeleteJwtTokenPort;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.member.application.port.out.SaveJwtTokenPort;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationMemberUseCase {

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
		saveRefreshToken(email, address, jwtTokenDto.getRefreshToken());

		return jwtTokenDto;
	}

	@Override
	public boolean logout(String email, String address) {
		deleteJwtTokenPort.delete(email, address);

		return true;
	}

	@Override
	public boolean logoutAll(String email) {
		deleteJwtTokenPort.deleteAll(email);

		return true;
	}

	@Override
	public JwtTokenDto updateJwtToken(String refreshToken) {
		checkWrongRefreshToken(refreshToken);

		return renewAndSaveJwtTokens(refreshToken);
	}

	private void checkWrongRefreshToken(String refreshToken) {
		try {
			jwtTokenProvider.verifyToken(refreshToken);
		} catch (RuntimeException exception) {
			throw new IllegalArgumentException("잘못된 토큰입니다.");
		}
	}

	private JwtTokenDto renewAndSaveJwtTokens(String refreshToken) {
		JwtPayloadDto jwtPayload = jwtTokenProvider.getJwtPayload(refreshToken);
		JwtTokenDto jwtTokenDto = createJwtTokenDto(jwtPayload.getEmail(), jwtPayload.getAddress());

		saveRefreshToken(jwtPayload.getEmail(), jwtPayload.getAddress(), jwtTokenDto.getRefreshToken());

		return jwtTokenDto;
	}

	private void saveRefreshToken(String email, String address, String refreshToken) {
		saveJwtTokenPort.save(email, address, refreshToken);
	}

	private JwtTokenDto createJwtTokenDto(String email, String address) {
		String accessToken = jwtTokenProvider.createToken(email, address, ACCESS_TOKEN);
		String refreshToken = jwtTokenProvider.createToken(email, address, REFRESH_TOKEN);

		return JwtTokenDto.of(accessToken, refreshToken);
	}

	private void isMatchPassword(String email, String password) {
		boolean result = passwordEncoder.matches(password, findPasswordByEmail(email));

		if (!result) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
	}

	private String findPasswordByEmail(String email) {
		return findMemberPort.findPasswordByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}
}
