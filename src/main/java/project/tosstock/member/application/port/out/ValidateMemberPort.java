package project.tosstock.member.application.port.out;

public interface ValidateMemberPort {

	boolean isDuplicatedEmail(String email);

	boolean isExistPhoneNumber(String phoneNumber);

}
