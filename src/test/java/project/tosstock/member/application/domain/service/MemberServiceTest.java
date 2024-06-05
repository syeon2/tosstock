package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.common.error.exception.DuplicatedAccountException;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.domain.model.UpdateMemberDto;
import project.tosstock.member.application.port.out.AuthCodeByMailPort;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AuthCodeByMailPort authCodeByMailPort;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "회원 도메인을 parameter로 받아 Repository에 저장합니다.")
	void joinMember() {
		// given
		Member member = createMember("gsy94@gmail.com", "01029391234");
		String authCode = "000000";

		given(authCodeByMailPort.findAuthCodeByMail(anyString()))
			.willReturn(authCode);

		// when
		Long savedMemberId = memberService.joinMember(member, authCode);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(savedMemberId);
		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getId()).isEqualTo(savedMemberId));
	}

	@Test
	@DisplayName(value = "이미 가입된 이메일로 가입 시 예외를 반환합니다.")
	void joinMember_exception_duplicatedEmail() {
		// given
		String email = "waterkite94@gmail.com";
		Member member = createMember(email, "01011112222");
		String authCode = "000000";

		given(authCodeByMailPort.findAuthCodeByMail(anyString()))
			.willReturn(authCode);

		memberService.joinMember(member, authCode);

		// when
		Member duplicatedEmailMember = createMember(email, "01022223333");

		// then
		assertThatThrownBy(() -> memberService.joinMember(duplicatedEmailMember, authCode))
			.isInstanceOf(DuplicatedAccountException.class)
			.hasMessage("이미 가입된 회원입니다.");
	}

	@Test
	@DisplayName(value = "이미 가입된 전화번호로 가입 시 예외를 반환합니다.")
	void joinMember_exception_duplicatedPhoneNumber() {
		// given
		String phoneNumber = "01011112222";
		Member member = createMember("waterkite94@gmail.com", phoneNumber);
		String authCode = "000000";

		given(authCodeByMailPort.findAuthCodeByMail(anyString()))
			.willReturn(authCode);

		memberService.joinMember(member, authCode);

		// when
		Member duplicatedPhoneNumberMember = createMember("gsy4568@gmail.com", phoneNumber);

		// then
		assertThatThrownBy(() -> memberService.joinMember(duplicatedPhoneNumberMember, authCode))
			.isInstanceOf(DuplicatedAccountException.class)
			.hasMessage("이미 가입된 회원입니다.");
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경합니다.")
	void updatePassword() {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		MemberEntity entity = createMemberEntity(email, password);

		memberRepository.save(entity);

		// when
		String changedPassword = "987654321";
		memberService.changePassword(email, changedPassword);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(entity.getId());

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(passwordEncoder.matches(password, s.getPassword())).isFalse());

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(passwordEncoder.matches(changedPassword, s.getPassword())).isTrue());
	}

	@Test
	@DisplayName(value = "회원 아이디와 변경할 DTO를 받고 회원 정보를 수정합니다.")
	void changeMemberInfo() {
		// given
		String username = "suyeon";
		String introduce = "안녕하세요";
		String profileImageUrl = "www.github.com/syeon2";
		MemberEntity member = createMemberEntity(username, introduce, profileImageUrl);

		Long savedMemberId = memberRepository.save(member).getId();

		String changeName = "kim!";
		String changeIntroduce = "반갑습니다.";
		String changeProfileImageUrl = "www.syeon2.github.io";
		UpdateMemberDto updateMemberDto = createUpdateMemberDto(changeName, changeIntroduce, changeProfileImageUrl);

		// when
		memberService.changeMemberInfo(savedMemberId, updateMemberDto);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(savedMemberId);

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(m -> assertThat(m.getUsername()).isEqualTo(changeName))
			.hasValueSatisfying(m -> assertThat(m.getIntroduce()).isEqualTo(changeIntroduce))
			.hasValueSatisfying(m -> assertThat(m.getProfileImageUrl()).isEqualTo(changeProfileImageUrl));
	}

	private UpdateMemberDto createUpdateMemberDto(String changeName, String introduce, String profileImageUrl) {
		return UpdateMemberDto.builder()
			.username(changeName)
			.introduce(introduce)
			.profileImageUrl(profileImageUrl)
			.build();
	}

	private MemberEntity createMemberEntity(String email, String password) {
		return MemberEntity.builder()
			.email(email)
			.username("suyeon")
			.password(passwordEncoder.encode(password))
			.phoneNumber("00011112222")
			.build();
	}

	private MemberEntity createMemberEntity(String username, String introduce, String profileImageUrl) {
		return MemberEntity.builder()
			.email("www@wwww")
			.username(username)
			.password("1231321312321")
			.phoneNumber("00011112222")
			.introduce(introduce)
			.profileImageUrl(profileImageUrl)
			.build();
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
