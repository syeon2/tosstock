package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.adapter.out.persistence.RedisRefreshTokenRepository;
import project.tosstock.member.application.domain.model.JwtTokenDto;

class AuthServiceTest extends IntegrationTestSupport {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RedisRefreshTokenRepository redisRefreshTokenRepository;

	@Autowired
	@Qualifier(value = "redisJwtTokenTemplate")
	private RedisTemplate<String, Object> redisAuthTokenTemplate;

	@Autowired
	private PasswordEncoder encoder;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
		redisAuthTokenTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@Test
	@DisplayName(value = "올바른 이메일과 비밀번호를 통해 로그인을 성공합니다.")
	void login() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		MemberEntity member = createMember(email, password);
		memberRepository.save(member);

		// when
		JwtTokenDto tokenDto = authService.login(email, password, address);

		// then
		assertThat(tokenDto).isNotNull();
		assertThat(tokenDto.getAccessToken()).isInstanceOf(String.class);
		assertThat(tokenDto.getRefreshToken()).isInstanceOf(String.class);

		Optional<String> findTokenByEmailAndAddress = redisRefreshTokenRepository.findTokenByEmailAndAddress(email,
			address);

		assertThat(findTokenByEmailAndAddress).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(tokenDto.getRefreshToken()));
	}

	@Test
	@DisplayName(value = "회원가입하지 않은 이메일로 로그인할 시 예외가 발생합니다.")
	void login_exception_non_email() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		// when  // then
		assertThatThrownBy(() -> authService.login(email, password, address))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 회원입니다.");
	}

	@Test
	@DisplayName(value = "비밀번호가 일치하지 않으면 예외를 반환합니다.")
	void login_exception_wrong_password() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";

		MemberEntity member = createMember(email, password);
		memberRepository.save(member);

		// when  // then
		assertThatThrownBy(() -> authService.login(email, "11111111", "1"))
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

		MemberEntity member = createMember(email, password);
		memberRepository.save(member);
		JwtTokenDto tokenDto = authService.login(email, password, address);
		;

		assertThat(redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address)).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(tokenDto.getRefreshToken()));

		// when
		authService.logout(email, address);

		// then
		Optional<String> findTokenByEmailAndAddress
			= redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address);

		assertThat(findTokenByEmailAndAddress).isEmpty();
	}

	@Test
	@DisplayName(value = "모든 기기 로그아웃을 하면 Redis에 저장되어 있는 모든 Refresh Token이 삭제됩니다.")
	void logout_all() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address1 = "1";
		String address2 = "2";

		MemberEntity member = createMember(email, password);
		memberRepository.save(member);
		JwtTokenDto tokenDto1 = authService.login(email, password, address1);
		JwtTokenDto tokenDto2 = authService.login(email, password, address2);

		assertThat(redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address1)).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(tokenDto1.getRefreshToken()));

		assertThat(redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address2)).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(tokenDto2.getRefreshToken()));

		// when
		authService.logoutAll(email);

		// then
		Optional<String> findTokenByEmailAndAddress1
			= redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address1);

		Optional<String> findTokenByEmailAndAddress2
			= redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address2);

		assertThat(findTokenByEmailAndAddress1).isEmpty();
		assertThat(findTokenByEmailAndAddress2).isEmpty();
	}

	@Test
	@DisplayName(value = "Refresh Token을 받으면 새로운 JWT 토큰으로 갱신되고 Repository에 저장됩니다.")
	void update_jwt_token() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		MemberEntity member = createMember(email, password);
		memberRepository.save(member);
		JwtTokenDto tokenDto = authService.login(email, password, address);

		assertThat(redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address)).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(tokenDto.getRefreshToken()));

		// when
		JwtTokenDto updatedJwtToken = authService.updateJwtToken(tokenDto.getRefreshToken());

		// then
		assertThat(redisRefreshTokenRepository.findTokenByEmailAndAddress(email, address)).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(updatedJwtToken.getRefreshToken()));
	}

	@Test
	@DisplayName(value = "잘못된 Refresh Token을 받으면 예외를 반환합니다.")
	void update_jwt_token_wrong_token() {
		// given
		String refreshToken = "refresh_token";

		// when  // then
		assertThatThrownBy(() -> authService.updateJwtToken(refreshToken))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 토큰입니다.");
	}

	private MemberEntity createMember(String email, String password) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password(encoder.encode(password))
			.phoneNumber("01011112222")
			.introduce("반갑습니다.")
			.profileImageUrl("www.naver.com")
			.build();
	}
}
