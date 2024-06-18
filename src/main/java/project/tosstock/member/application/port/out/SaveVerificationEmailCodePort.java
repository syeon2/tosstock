package project.tosstock.member.application.port.out;

public interface SaveVerificationEmailCodePort {

	void save(String email, String code);
}
