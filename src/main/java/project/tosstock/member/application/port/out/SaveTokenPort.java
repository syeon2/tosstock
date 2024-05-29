package project.tosstock.member.application.port.out;

public interface SaveTokenPort {

	void save(String email, String address, String token);

	void mergeByEmailAndToken(String email, String prevToken, String renewToken);
}
