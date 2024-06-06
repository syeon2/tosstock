package project.tosstock.member.application.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.port.in.SendAuthCodeUseCase;
import project.tosstock.member.application.port.out.SaveAuthCodePort;

@Service
@RequiredArgsConstructor
public class MailService implements SendAuthCodeUseCase {

	@Value("${spring.mail.title}")
	private String mailTitle;

	@Value("${spring.mail.code-length}")
	private Integer codeLength;

	private final SaveAuthCodePort saveAuthCodePort;

	private final JavaMailSender javaMailSender;

	@Override
	public boolean dispatchAuthCodeToEmail(String toEmail) {
		String authCode = MailUtil.createAuthCode(codeLength);

		sendMail(toEmail, authCode);
		saveAuthCode(toEmail, authCode);

		return true;
	}

	private void saveAuthCode(String toEmail, String authCode) {
		saveAuthCodePort.save(toEmail, authCode);
	}

	private void sendMail(String toEmail, String authCode) {
		SimpleMailMessage emailForm = MailUtil.createEmailForm(toEmail, mailTitle, authCode);

		javaMailSender.send(emailForm);
	}
}
