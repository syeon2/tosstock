package project.tosstock.member.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;

class MemberPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private MemberPersistenceAdapter memberPersistenceAdapter;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "회원 도메인을 parameter로 받아 Repository에 저장합니다.")
	void save_member() {
		// given
		Member member = createMember("waterkite94@gmail.com", "01000001111");

		// when
		Long savedMemberId = memberPersistenceAdapter.save(member);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(savedMemberId);
		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getId()).isEqualTo(savedMemberId));
	}

	@Test
	@DisplayName(value = "이메일에 대한 비밀번호를 가져옵니다.")
	void find_password_by_email() {
		// given
		String email = "waterkite94@gmail.com";
		String encodedPassword = passwordEncoder.encode("12345678");

		Member member = createMember(email, encodedPassword, "01011112222");
		memberPersistenceAdapter.save(member);

		// when
		String findPassword = memberPersistenceAdapter.findPasswordByEmail(email);

		// then
		assertThat(findPassword).isEqualTo(encodedPassword);
	}

	@Test
	@DisplayName(value = "이메일이 없을 경우 null을 반환합니다.")
	void find_password_by_email_null() {
		// given
		String email = "waterkite94@gmail.com";

		// when  // then
		assertThatThrownBy(() -> memberPersistenceAdapter.findPasswordByEmail(email))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 이메일입니다.");
	}

	@Test
	@DisplayName(value = "회원 이름을 변경합니다.")
	void update_username() {
		// given
		Member member =
			createMember("waterkite94@gmail.com", "suyeon", "12345678", "https://syeon2.github.io/");
		Long saveMemberId = memberPersistenceAdapter.save(member);

		// when
		String changedUsername = "kimsuyeon";
		memberPersistenceAdapter.updateUsername(saveMemberId, changedUsername);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s.getUsername()).isEqualTo(changedUsername));
	}

	@Test
	@DisplayName(value = "회원 프로필 이미지 URL을 변경합니다.")
	void update_url() {
		// given
		String url = "https://syeon2.github.io/";
		Member member =
			createMember("waterkite94@gmail.com", "suyeon", "12345678", url);
		Long saveMemberId = memberPersistenceAdapter.save(member);

		// when
		String changedUrl = "https://github.com/syeon2";
		memberPersistenceAdapter.updateProfileImageUrl(saveMemberId, changedUrl);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s.getProfileImageUrl()).isEqualTo(changedUrl));
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경합니다.")
	void update_password() {
		// given
		Member member =
			createMember("waterkite94@gmail.com", "suyeon", "12345678", "https://syeon2.github.io/");
		Long saveMemberId = memberPersistenceAdapter.save(member);

		// when
		String changedPassword = "987654321";
		String encodedPassword = passwordEncoder.encode(changedPassword);
		memberPersistenceAdapter.updatePassword(saveMemberId, encodedPassword);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s.getPassword()).isEqualTo(encodedPassword));
	}

	@Test
	@DisplayName(value = "회원 아이디로 회원을 조회합니다.")
	void find_member_by_Id() {
		// given
		Member member = createMember("waterkite94@gmail.com", "01000001111");
		Long savedMemberId = memberPersistenceAdapter.save(member);

		// when
		MemberEntity findMember = memberPersistenceAdapter.findMemberById(savedMemberId);

		// then
		assertThat(findMember.getId()).isEqualTo(savedMemberId);
	}

	@Test
	@DisplayName(value = "존재하지 않은 회원을 조회했을 경우 예외를 반환합니다.")
	void find_member_by_id_exception_null() {
		// given
		Long memberId = 1L;

		// when  // then
		assertThatThrownBy(() -> memberPersistenceAdapter.findMemberById(memberId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 회원입니다.");
	}

	@Test
	@DisplayName(value = "이메일과 전화번호 둘 중 하나의 값과 일치하는 회원을 조회합니다.")
	void find_member_by_email_or_phoneNumber() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";

		Member member = createMember(email, phoneNumber);
		memberPersistenceAdapter.save(member);

		// when
		Optional<MemberEntity> findMemberOptional1 =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber("gsy4568@gmailc.om", phoneNumber);

		Optional<MemberEntity> findMemberOptional2 =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber(email, "01020204949");

		Optional<MemberEntity> findMemberOptional3 =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber("345@gmail.com", "18012841123");

		// then
		assertThat(findMemberOptional1).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getPhoneNumber()).isEqualTo(phoneNumber));

		assertThat(findMemberOptional2).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getEmail()).isEqualTo(email));

		assertThat(findMemberOptional3).isEmpty();
	}

	private Member createMember(String email, String phoneNumber) {
		return Member.builder()
			.username("suyeon")
			.email(email)
			.password(passwordEncoder.encode("12345678"))
			.phoneNumber(phoneNumber)
			.introduce("반갑습니다.")
			.profileImageUrl("www.naver.com")
			.build();
	}

	private Member createMember(String email, String username, String password,
		String profileImageUrl) {
		return Member.builder()
			.username(username)
			.email(email)
			.password(passwordEncoder.encode(password))
			.phoneNumber("01000001111")
			.introduce("반갑습니다.")
			.profileImageUrl(profileImageUrl)
			.build();
	}

	private Member createMember(String email, String password, String phoneNumber) {
		return Member.builder()
			.username("suyeon")
			.email(email)
			.password(password)
			.phoneNumber(phoneNumber)
			.introduce("반갑습니다.")
			.profileImageUrl("www.naver.com")
			.build();
	}
}
