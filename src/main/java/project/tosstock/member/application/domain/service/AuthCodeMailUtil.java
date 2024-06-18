package project.tosstock.member.application.domain.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCodeMailUtil {

	private final JavaMailSender javaMailSender;

	@Async(value = "threadPoolTaskExecutorForMail")
	public void sendMailByJavaMailSender(String toEmail, String title, String text) {
		SimpleMailMessage emailForm = AuthCodeMailUtil.createEmailForm(toEmail, title, text);

		try {
			javaMailSender.send(emailForm);
		} catch (Exception e) {
			log.error("---Exception Auth Code Mail Sender---");
		}
	}

	private static SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);

		return message;
	}
}
