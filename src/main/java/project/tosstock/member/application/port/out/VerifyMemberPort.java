package project.tosstock.member.application.port.out;

public interface VerifyMemberPort {

	boolean isDuplicatedEmail(String email);

	boolean isExistPhoneNumber(String phoneNumber);

}
