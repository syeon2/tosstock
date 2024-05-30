package project.tosstock.member.application.port.out;

public interface DeleteJwtTokenPort {

	void deleteOne(String email, String address);

	void deleteAll(String email);
}
