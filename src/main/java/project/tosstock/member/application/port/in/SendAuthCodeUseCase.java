package project.tosstock.member.application.port.in;

public interface SendAuthCodeUseCase {

	boolean dispatchAuthCodeToEmail(String toEmail);
}
