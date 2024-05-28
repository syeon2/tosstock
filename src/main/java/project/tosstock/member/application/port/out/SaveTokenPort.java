package project.tosstock.member.application.port.out;

import java.time.LocalDateTime;

public interface SaveTokenPort {

	void save(String email, String token, LocalDateTime expiredTime);
}
