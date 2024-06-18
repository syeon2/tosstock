package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.member.application.port.out.SaveMemberPort;
import project.tosstock.member.application.port.out.UpdateMemberPort;

@Transactional
class AuthenticationServiceTest extends IntegrationTestSupport {

	@Autowired
	private AuthenticationService authenticationService;

	@MockBean
	private FindMemberPort findMemberPort;

	@MockBean
	private SaveMemberPort saveMemberPort;

	@MockBean
	private UpdateMemberPort updateMemberPort;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier(value = "redisRefreshTokenTemplate")
	private RedisTemplate<String, Object> redisAuthTokenTemplate;

	@BeforeEach
	void before() {
		redisAuthTokenTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@Test
	@DisplayName(value = "올바른 이메일과 비밀번호를 통해 로그인을 성공합니다.")
	void login() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "1234";
		String address = "1";

		given(findMemberPort.findPasswordByEmail(any()))
			.willReturn(Optional.of(password));

		given(passwordEncoder.matches(any(), any()))
			.willReturn(true);

		// when
		JwtTokenDto tokenDto = authenticationService.login(email, password, address);

		// then
		assertThat(tokenDto.getAccessToken()).isNotNull().isInstanceOf(String.class);
		assertThat(tokenDto.getRefreshToken()).isNotNull().isInstanceOf(String.class);

		Object refreshToken = redisAuthTokenTemplate.opsForHash().get(email, address);

		assertThat((String)refreshToken).isEqualTo(tokenDto.getRefreshToken());
	}

	@Test
	@DisplayName(value = "회원가입하지 않은 이메일로 로그인할 시 예외가 발생합니다.")
	void login_exception_nonEmail() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		// when  // then
		assertThatThrownBy(() -> authenticationService.login(email, password, address))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 회원입니다.");
	}

	@Test
	@DisplayName(value = "비밀번호가 일치하지 않으면 예외를 반환합니다.")
	void login_exception_wrongPassword() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";

		given(findMemberPort.findPasswordByEmail(any()))
			.willReturn(Optional.of(password));

		// when  // then
		assertThatThrownBy(() -> authenticationService.login(email, "11111111", "1"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 비밀번호입니다.");
	}

	@Test
	@DisplayName(value = "로그아웃을 하면 Redis에 저장되어 있는 Refresh Token이 삭제됩니다.")
	void logout() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		given(findMemberPort.findPasswordByEmail(any()))
			.willReturn(Optional.of(password));

		given(passwordEncoder.matches(any(), any()))
			.willReturn(true);

		JwtTokenDto tokenDto = authenticationService.login(email, password, address);

		assertThat((String)redisAuthTokenTemplate.opsForHash().get(email, address))
			.isEqualTo(tokenDto.getRefreshToken());

		// when
		authenticationService.logout(email, address);

		// then
		Object findTokenByEmailAndAddress = redisAuthTokenTemplate.opsForHash().get(email, address);

		assertThat(findTokenByEmailAndAddress).isNull();
	}

	@Test
	@DisplayName(value = "모든 기기 로그아웃을 하면 Redis에 저장되어 있는 모든 Refresh Token이 삭제됩니다.")
	void logoutAll() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address1 = "1";
		String address2 = "2";

		given(findMemberPort.findPasswordByEmail(any()))
			.willReturn(Optional.of(password));

		given(passwordEncoder.matches(any(), any()))
			.willReturn(true);

		JwtTokenDto tokenDto1 = authenticationService.login(email, password, address1);
		JwtTokenDto tokenDto2 = authenticationService.login(email, password, address2);

		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address1)).isEqualTo(tokenDto1.getRefreshToken());
		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address2)).isEqualTo(tokenDto2.getRefreshToken());

		// when
		authenticationService.logoutAll(email);

		// then
		Object findToken1 = redisAuthTokenTemplate.opsForHash().get(email, address1);
		Object findToken2 = redisAuthTokenTemplate.opsForHash().get(email, address2);

		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address1)).isNull();
		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address2)).isNull();
	}

	@Test
	@DisplayName(value = "Refresh Token을 받으면 새로운 JWT 토큰으로 갱신되고 Repository에 저장됩니다.")
	void updateJwtToken() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		given(findMemberPort.findPasswordByEmail(any()))
			.willReturn(Optional.of(password));

		given(passwordEncoder.matches(any(), any()))
			.willReturn(true);

		JwtTokenDto tokenDto = authenticationService.login(email, password, address);

		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address)).isEqualTo(tokenDto.getRefreshToken());

		// when
		JwtTokenDto updatedJwtToken = authenticationService.updateJwtToken(tokenDto.getRefreshToken());

		// then
		assertThat(redisAuthTokenTemplate.opsForHash().get(email, address))
			.isEqualTo(updatedJwtToken.getRefreshToken());
	}

	@Test
	@DisplayName(value = "잘못된 Refresh Token을 받으면 예외를 반환합니다.")
	void updateJwtToken_exception_wrongToken() {
		// given
		String refreshToken = "refresh_token";

		// when  // then
		assertThatThrownBy(() -> authenticationService.updateJwtToken(refreshToken))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 토큰입니다.");
	}
}
