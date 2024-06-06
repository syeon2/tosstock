package project.tosstock.member.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.domain.model.UpdateMemberDto;

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
	void save() {
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
	@DisplayName(value = "이메일을 통해 비밀번호를 가져옵니다.")
	void findPasswordByEmail() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "123456789012345678901234567890123456789012345678901234567890";
		Member member = createMember(email, password, "01011112222");

		memberPersistenceAdapter.save(member);

		// when
		Optional<String> findPasswordOptional = memberPersistenceAdapter.findPasswordByEmail(email);

		// then
		assertThat(findPasswordOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s).isEqualTo(password));
	}

	@Test
	@DisplayName(value = "이메일이 없을 경우 null을 반환합니다.")
	void findPasswordByEmail_null() {
		// given
		String email = "waterkite94@gmail.com";

		// when
		Optional<String> findPasswordByEmail = memberPersistenceAdapter.findPasswordByEmail(email);

		// then
		assertThat(findPasswordByEmail).isEmpty();
	}

	@Test
	@DisplayName(value = "회원 아이디로 회원을 조회합니다.")
	void findMemberById() {
		// given
		Member member = createMember("waterkite94@gmail.com", "01000001111");
		Long savedMemberId = memberPersistenceAdapter.save(member);

		// when
		Optional<Member> findMember = memberPersistenceAdapter.findMemberById(savedMemberId);

		// then
		assertThat(findMember).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getId()).isEqualTo(savedMemberId));
	}

	@Test
	@DisplayName(value = "존재하지 않은 회원을 조회했을 경우 null을 반환합니다.")
	void findMemberById_null() {
		// given
		Long memberId = 1L;

		// when
		Optional<Member> findMemberById = memberPersistenceAdapter.findMemberById(memberId);

		// then
		assertThat(findMemberById).isEmpty();
	}

	@Test
	@DisplayName(value = "이메일과 전화번호 둘 중 이메일만 일치하는 회원을 조회합니다.")
	void findMemberByEmailOrPhoneNumber_onlyEmail() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";
		Member member = createMember(email, phoneNumber);

		memberPersistenceAdapter.save(member);

		// when
		String changePhoneNumber = "01020204949";
		Optional<Member> findMemberOptional =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber(email, changePhoneNumber);

		// then
		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getEmail()).isEqualTo(email));
	}

	@Test
	@DisplayName(value = "이메일과 전화번호 둘 중 전화번호만 일치하는 회원을 조회합니다.")
	void findMemberByEmailOrPhoneNumber_onlyPhoneNumber() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";
		Member member = createMember(email, phoneNumber);

		memberPersistenceAdapter.save(member);

		// when
		String changeEmail = "gsy4568@gmailc.om";
		Optional<Member> findMemberOptional =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber(changeEmail, phoneNumber);

		// then
		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getPhoneNumber()).isEqualTo(phoneNumber));
	}

	@Test
	@DisplayName("이메일과 전화번호 중 아무것도 일치하지 않다면 null을 반환합니다.")
	void findMemberByEmailOrPhoneNumber_null() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";
		Member member = createMember(email, phoneNumber);

		memberPersistenceAdapter.save(member);

		// when
		String changeEmail = "345@gmail.com";
		String changePhoneNumber = "18012841123";
		Optional<Member> findMemberOptional =
			memberPersistenceAdapter.findMemberByEmailOrPhoneNumber(changeEmail, changePhoneNumber);

		// then
		assertThat(findMemberOptional).isEmpty();
	}

	@Test
	@DisplayName(value = "update Member Dto를 받아 기존 회원의 정보(이름)를 변경합니다.")
	void updateMemberInfo_username() {
		// given
		String username = "suyeon";
		String introduce = "반갑습니다";
		String profileImageUrl = "www.github.com/syeon2";
		Member member = createMember(username, "01011112222", introduce, profileImageUrl);

		Long saveMemberId = memberPersistenceAdapter.save(member);

		String changeUsername = "kim!";
		UpdateMemberDto updateMemberDto = createUpdateMemberDto(changeUsername, null, profileImageUrl);

		// when
		memberPersistenceAdapter.updateInfo(saveMemberId, updateMemberDto);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getUsername()).isEqualTo(changeUsername))
			.hasValueSatisfying(m -> assertThat(m.getIntroduce()).isEqualTo(introduce))
			.hasValueSatisfying(m -> assertThat(m.getProfileImageUrl()).isEqualTo(profileImageUrl));
	}

	@Test
	@DisplayName(value = "update Member Dto를 받아 기존 회원의 정보(자기 소개)를 업데이트합니다.")
	void updateMemberInfo_introduce() {
		// given
		String username = "suyeon";
		String introduce = "반갑습니다";
		String profileImageUrl = "www.github.com/syeon2";
		Member member = createMember(username, "01011112222", introduce, profileImageUrl);

		Long saveMemberId = memberPersistenceAdapter.save(member);

		String changeIntroduce = "안녕하세요!";
		UpdateMemberDto updateMemberDto = createUpdateMemberDto(username, changeIntroduce, profileImageUrl);

		// when
		memberPersistenceAdapter.updateInfo(saveMemberId, updateMemberDto);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getUsername()).isEqualTo(username))
			.hasValueSatisfying(m -> assertThat(m.getIntroduce()).isEqualTo(changeIntroduce))
			.hasValueSatisfying(m -> assertThat(m.getProfileImageUrl()).isEqualTo(profileImageUrl));
	}

	@Test
	@DisplayName(value = "update Member Dto를 받아 기존 회원의 정보(프로필 이미지)를 업데이트합니다.")
	void updateMemberInfo_profileImageUrl() {
		// given
		String username = "suyeon";
		String introduce = "반갑습니다";
		String profileImageUrl = "www.github.com/syeon2";
		Member member = createMember(username, "01011112222", introduce, profileImageUrl);

		Long saveMemberId = memberPersistenceAdapter.save(member);

		String changeProfileImageUrl = "https://syeon2.github.io/";
		UpdateMemberDto updateMemberDto = createUpdateMemberDto(null, null, changeProfileImageUrl);

		// when
		memberPersistenceAdapter.updateInfo(saveMemberId, updateMemberDto);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getUsername()).isEqualTo(username))
			.hasValueSatisfying(m -> assertThat(m.getIntroduce()).isEqualTo(introduce))
			.hasValueSatisfying(m -> assertThat(m.getProfileImageUrl()).isEqualTo(changeProfileImageUrl));
	}

	@Test
	@DisplayName(value = "update Member Dto를 받아 기존 회원의 정보(모두)를 업데이트합니다.")
	void updateMemberInfo_all() {
		// given
		String username = "suyeon";
		String introduce = "반갑습니다";
		String profileImageUrl = "www.github.com/syeon2";
		Member member = createMember(username, "01011112222", introduce, profileImageUrl);

		Long saveMemberId = memberPersistenceAdapter.save(member);

		String changeUsername = "kim!";
		String changeIntroduce = "안녕하세요!";
		String changeProfileImageUrl = "https://syeon2.github.io/";
		UpdateMemberDto updateMemberDto = createUpdateMemberDto(changeUsername, changeIntroduce, changeProfileImageUrl);

		// when
		memberPersistenceAdapter.updateInfo(saveMemberId, updateMemberDto);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(saveMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getUsername()).isEqualTo(changeUsername))
			.hasValueSatisfying(m -> assertThat(m.getIntroduce()).isEqualTo(changeIntroduce))
			.hasValueSatisfying(m -> assertThat(m.getProfileImageUrl()).isEqualTo(changeProfileImageUrl));
	}

	@Test
	@DisplayName(value = "비밀번호를 받아 변경합니다.")
	void updatePassword() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "123456789012345678901234567890123456789012345678901234567890";
		Member member = createMember(email, password, "00011112222");

		Long savedMemberId = memberPersistenceAdapter.save(member);

		String changePassword = "123456789012345678901234567890123456789012345678901234511111";

		// when
		memberPersistenceAdapter.updatePassword(email, changePassword);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(savedMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getPassword()).isEqualTo(changePassword).isNotEqualTo(password));
	}

	private static UpdateMemberDto createUpdateMemberDto(String changeUsername, String changeIntroduce,
		String changeProfileImageUrl) {
		UpdateMemberDto updateMemberDto = UpdateMemberDto.builder()
			.username(changeUsername)
			.introduce(changeIntroduce)
			.profileImageUrl(changeProfileImageUrl)
			.build();
		return updateMemberDto;
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

	private Member createMember(String username, String phoneNumber, String introduce, String profileImageUrl) {
		return Member.builder()
			.username(username)
			.email("waterkite94@mgila.com")
			.password("123456768")
			.phoneNumber(phoneNumber)
			.introduce(introduce)
			.profileImageUrl(profileImageUrl)
			.build();
	}
}
