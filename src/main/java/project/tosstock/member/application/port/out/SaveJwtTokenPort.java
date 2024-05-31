package project.tosstock.member.application.port.out;

public interface SaveJwtTokenPort {

	void save(String email, String address, String token);
}
