package project.tosstock.member.application.port.in;

public interface SendAuthCodeByMailUseCase {

	boolean sendEmail(String toEmail);
}
