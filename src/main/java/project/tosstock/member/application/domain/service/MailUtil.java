package project.tosstock.member.application.domain.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;

public class MailUtil {

	public static String createAuthCode(int codeLength) {
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

	public static SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);

		return message;
	}
}
