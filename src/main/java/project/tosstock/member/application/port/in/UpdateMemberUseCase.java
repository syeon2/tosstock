package project.tosstock.member.application.port.in;

public interface UpdateMemberUseCase {

	boolean changeUsername(Long id, String username);

	boolean changeProfileImageUrl(Long id, String profileImageUrl);

	boolean changePassword(Long id, String email, String password);
}
