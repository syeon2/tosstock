package project.tosstock.member.application.port.out;

public interface SaveAuthCodePort {

	void save(String email, String code);
}
