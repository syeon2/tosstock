package project.tosstock.member.application.port.out;

public interface UpdateMemberPort {

	void updateUsername(Long id, String username);

	void updateProfileImageUrl(Long id, String profileImageUrl);

	void updatePassword(Long id, String password);
}
