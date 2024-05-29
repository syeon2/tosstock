package project.tosstock.member.application.port.out;

public interface DeleteTokenPort {

	void deleteOne(String email, String address);

	void deleteAll(String email);
}
