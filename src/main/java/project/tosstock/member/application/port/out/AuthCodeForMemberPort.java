package project.tosstock.member.application.port.out;

import java.util.Optional;

public interface AuthCodeForMemberPort {

	void saveAuthCode(String email, String code);

	Optional<String> findAuthCodeByEmail(String email);
}
