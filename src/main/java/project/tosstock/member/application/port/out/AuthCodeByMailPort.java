package project.tosstock.member.application.port.out;

public interface AuthCodeByMailPort {

	void save(String email, String code);

	String findAuthCodeByMail(String email);
}
