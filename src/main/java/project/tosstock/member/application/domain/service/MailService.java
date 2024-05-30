package project.tosstock.member.application.domain.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.port.in.SendAuthCodeByMailUseCase;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;

@Service
@RequiredArgsConstructor
public class MailService implements SendAuthCodeByMailUseCase {

	@Value("${spring.mail.title}")
	private String mailTitle;

	@Value("${spring.mail.code-length}")
	private Integer codeLength;

	private final JavaMailSender javaMailSender;
	private final AuthCodeByMailPort authCodeByMailPort;

	@Override
	public boolean sendEmail(String toEmail) {
		String code = createCode();
		SimpleMailMessage emailForm = createEmailForm(toEmail, mailTitle, code);

		javaMailSender.send(emailForm);
		authCodeByMailPort.saveAuthCode(toEmail, code);

		return true;
	}

	private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);

		return message;
	}

	private String createCode() {
		try {
			Random random = SecureRandom.getInstanceStrong();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < codeLength; i++) {
				builder.append(random.nextInt(10));
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Email Exception originate from MailService");
		}
	}
}
