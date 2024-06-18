package project.tosstock.member.application.domain.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.port.in.SendVerificationEmailCodeUseCase;
import project.tosstock.member.application.port.out.SaveVerificationEmailCodePort;

@Service
@RequiredArgsConstructor
public class VerificationServiceEmail implements SendVerificationEmailCodeUseCase {

	@Value("${spring.mail.title}")
	private String mailTitle;

	@Value("${spring.mail.code-length}")
	private Integer codeLength;

	private final SaveVerificationEmailCodePort saveVerificationEmailCodePort;
	private final VerificationEmailUtil verificationEmailUtil;

	@Override
	public boolean sendAuthCodeToEmail(String toEmail) {
		String authCode = createAuthCode(codeLength);

		saveVerificationEmailCodePort.save(toEmail, authCode);
		verificationEmailUtil.sendMailByJavaMailSender(toEmail, mailTitle, authCode);

		return true;
	}

	private static String createAuthCode(int codeLength) {
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
