package project.tosstock.member.application.port.out;

import java.util.Optional;

public interface FindAuthCodePort {

	Optional<String> findAuthCodeByMail(String email);
	
}
