package project.tosstock.member.application.port.out;

import java.util.Optional;

public interface LoginPort {

	Optional<String> findPasswordByEmail(String email);
}
