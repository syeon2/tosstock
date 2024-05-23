package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.common.error.exception.DuplicateAccountException;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;
import project.tosstock.member.application.port.out.AuthCodeForMemberPort;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@MockBean
	private AuthCodeForMemberPort authCodeForMemberPort;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "회원 도메인을 parameter로 받아 Repository에 저장합니다.")
	void join_member() {
		// given
		Member member = createMember("waterkite94@gmail.com", "01011112222");
		String authCode = "000000";

		given(authCodeForMemberPort.findAuthCodeByEmail(anyString()))
			.willReturn(Optional.of(authCode));

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

		given(authCodeForMemberPort.findAuthCodeByEmail(anyString()))
			.willReturn(Optional.of(authCode));

		memberService.joinMember(member1, authCode);

		// when
		Member member2 = createMember(email, "01022223333");

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2, authCode))
			.isInstanceOf(DuplicateAccountException.class)
			.hasMessage("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName(value = "이미 가입된 전화번호로 가입 시 예외를 반환합니다.")
	void join_member_exception_phoneNumber() {
		// given
		String email = "waterkite94@gmail.com";
		String phoneNumber = "01011112222";
		Member member1 = createMember(email, phoneNumber);

		String authCode = "000000";
		given(authCodeForMemberPort.findAuthCodeByEmail(anyString()))
			.willReturn(Optional.of(authCode));

		memberService.joinMember(member1, authCode);

		// when
		Member member2 = createMember("gsy4568@gmail.com", phoneNumber);

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2, authCode))
			.isInstanceOf(DuplicateAccountException.class)
			.hasMessage("이미 가입된 전화번호입니다.");
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
