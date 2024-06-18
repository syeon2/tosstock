package project.tosstock.member.application.port.in;

public interface SendAuthCodeUseCase {

	boolean sendAuthCodeToEmail(String toEmail);
}
