package project.tosstock.member.application.port.out;

public interface DeleteJwtTokenPort {

	void delete(String email, String address);

	void deleteAll(String email);
}
