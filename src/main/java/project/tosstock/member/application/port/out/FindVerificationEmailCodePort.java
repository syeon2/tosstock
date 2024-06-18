package project.tosstock.member.application.port.out;

import java.util.Optional;

public interface FindVerificationEmailCodePort {

	Optional<String> findAuthCodeByMail(String email);

}
