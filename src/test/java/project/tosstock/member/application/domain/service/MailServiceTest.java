package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;

class MailServiceTest extends IntegrationTestSupport {

	@Autowired
	private MailService mailService;

	@MockBean
	private JavaMailSender javaMailSender;

	@MockBean
	private AuthCodeForMemberPort authCodeForMemberPort;

	@Test
	@DisplayName(value = "이메일 주소를 받아 인증번호를 보냅니다.")
	void send_email() {
		// given
		String email = "waterkite94@gmail.com";
		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
		doNothing().when(authCodeForMemberPort).saveAuthCode(anyString(), anyString());

		// when
		boolean result = mailService.sendEmail(email);

		// then
		assertThat(result).isTrue();
	}
}
