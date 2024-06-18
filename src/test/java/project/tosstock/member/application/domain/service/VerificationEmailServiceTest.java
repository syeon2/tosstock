package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import project.tosstock.IntegrationTestSupport;

class VerificationEmailServiceTest extends IntegrationTestSupport {

	@Autowired
	private VerificationServiceEmail verificationEmailService;

	@MockBean
	private JavaMailSender javaMailSender;

	@Autowired
	@Qualifier(value = "redisVerificationEmailCodeTemplate")
	private RedisTemplate<String, String> redisVerificationEmailCodeTemplate;

	@BeforeEach
	void before() {
		redisVerificationEmailCodeTemplate.getConnectionFactory().getConnection().flushAll();
	}

	@Test
	@DisplayName(value = "이메일 주소를 받아 인증번호를 보냅니다.")
	void send_email() {
		// given
		String email = "waterkite94@gmail.com";
		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

		// when
		boolean result = verificationEmailService.sendAuthCodeToEmail(email);

		// then
		assertThat(result).isTrue();

		String findAuthCode = redisVerificationEmailCodeTemplate.opsForValue().get(email);
		assertThat(findAuthCode).isNotNull();
	}
}
