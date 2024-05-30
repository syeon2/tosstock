package project.tosstock.member.application.port.out;

import java.util.Optional;

public interface AuthCodeByMailPort {

	void saveAuthCode(String email, String code);

	Optional<String> findAuthCodeByMail(String email);
}
