package project.tosstock.member.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;

class MemberPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private MemberPersistenceAdapter memberPersistenceAdapter;

	@Autowired
	private MemberRepository memberRepository;

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
		Long savedMemberId = memberPersistenceAdapter.saveMember(member);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(savedMemberId);
		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getId()).isEqualTo(savedMemberId));
	}

	@Test
	@DisplayName(value = "저장소에 가입하고자 하는 이메일과 중복된 값이 있는지 확인합니다. (값이 없을 경우)")
	void is_duplicated_Email_Non() {
		// given
		String email = "waterkite94@gmail.com";

		// when
		boolean isDuplicated = memberPersistenceAdapter.isDuplicatedEmail(email);

		// then
		assertThat(isDuplicated).isFalse();
	}

	@Test
	@DisplayName(value = "저장소에 가입하고자 하는 이메일과 중복된 값이 있는지 확인합니다. (값이 있는 경우)")
	void is_duplicated_Email() {
		// given
		String email = "waterkite94@gmail.com";
		Member member = createMember(email, "01011112222");
		memberPersistenceAdapter.saveMember(member);

		// when
		boolean isDuplicated = memberPersistenceAdapter.isDuplicatedEmail(email);

		// then
		assertThat(isDuplicated).isTrue();
	}

	@Test
	@DisplayName(value = "저장소에 가입하고자 하는 전화번호와 중복된 값이 있는지 확인합니다. (값이 있는 경우)")
	void is_duplicated_PhoneNumber() {
		// given
		String phoneNumber = "01011112222";

		Member member = createMember("waterkite94@gmail.com", phoneNumber);
		memberPersistenceAdapter.saveMember(member);

		// when
		boolean isExist = memberPersistenceAdapter.isExistPhoneNumber(phoneNumber);

		// then
		assertThat(isExist).isTrue();
	}

	@Test
	@DisplayName(value = "저장소에 가입하고자 하는 전화번호와 중복된 값이 있는지 확인합니다. (값이 없는 경우)")
	void is_duplicated_PhoneNumber_Non() {
		// given
		String phoneNumber = "01011112222";

		// when
		boolean isExist = memberPersistenceAdapter.isExistPhoneNumber(phoneNumber);

		// then
		assertThat(isExist).isFalse();
	}

	private Member createMember(String email, String phoneNumber) {
		return Member.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber(phoneNumber)
			.introduce("반갑습니다.")
			.profileImageUrl("www.naver.com")
			.build();
	}
}
