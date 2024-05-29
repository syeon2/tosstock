package project.tosstock.member.application.port.in;

public interface UpdateMemberUseCase {

	void updateUsername(Long id, String username);

	void updateProfileImageUrl(Long id, String profileImageUrl);

	void updatePassword(Long id, String email, String password);
}
