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
	void join_member() {
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
	void join_member_exception_email() {
		// given
		String email = "waterkite94@gmail.com";
		Member member1 = createMember(email, "01011112222");
		String authCode = "000000";

		given(authCodeByMailPort.findAuthCodeByMail(anyString()))
			.willReturn(authCode);

		memberService.joinMember(member1, authCode);

		// when
		Member member2 = createMember(email, "01022223333");

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2, authCode))
			.isInstanceOf(DuplicatedAccountException.class)
			.hasMessage("이미 가입된 회원입니다.");
	}

	@Test
	@DisplayName(value = "이미 가입된 전화번호로 가입 시 예외를 반환합니다.")
	void join_member_exception_phoneNumber() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";
		Member member1 = createMember(email, phoneNumber);

		String authCode = "000000";
		given(authCodeByMailPort.findAuthCodeByMail(anyString()))
			.willReturn(authCode);

		memberService.joinMember(member1, authCode);

		// when
		Member member2 = createMember("gsy4568@gmail.com", phoneNumber);

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2, authCode))
			.isInstanceOf(DuplicatedAccountException.class)
			.hasMessage("이미 가입된 회원입니다.");
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경합니다.")
	void update_password() {
		// given
		String password = "12345678";
		String email = "waterkite94@gmail.com";
		MemberEntity entity = MemberEntity.builder()
			.email(email)
			.username("suyeon")
			.password(passwordEncoder.encode(password))
			.phoneNumber("00011112222")
			.build();

		memberRepository.save(entity);

		// when
		String changedPassword = "987654321";
		memberService.changePassword(entity.getId(), email, changedPassword);

		// then
		Optional<MemberEntity> findMemberOptional = memberRepository.findById(entity.getId());

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(passwordEncoder.matches(changedPassword, s.getPassword())).isTrue());

		assertThat(findMemberOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(passwordEncoder.matches(password, s.getPassword())).isFalse());
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
