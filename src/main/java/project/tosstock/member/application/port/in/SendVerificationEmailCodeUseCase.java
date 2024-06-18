package project.tosstock.member.application.port.in;

public interface SendVerificationEmailCodeUseCase {

	boolean sendAuthCodeToEmail(String toEmail);
}
