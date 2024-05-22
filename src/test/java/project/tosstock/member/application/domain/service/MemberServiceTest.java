package project.tosstock.member.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.common.error.exception.DuplicateAccountException;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.member.application.domain.model.Member;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "회원 도메인을 parameter로 받아 Repository에 저장합니다.")
	void join_member() {
		// given
		Member member = createMember("waterkite94@gmail.com", "01011112222");

		// when
		Long savedMemberId = memberService.joinMember(member);

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
		memberService.joinMember(member1);

		// when
		Member member2 = createMember(email, "01022223333");

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2))
			.isInstanceOf(DuplicateAccountException.class)
			.hasMessage("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName(value = "이미 가입된 전화번호로 가입 시 예외를 반환합니다.")
	void join_member_exception_phoneNumber() {
		// given
		String phoneNumber = "01011112222";

		Member member1 = createMember("waterkite94@gmail.com", phoneNumber);
		memberService.joinMember(member1);

		// when
		Member member2 = createMember("gsy4568@gmail.com", phoneNumber);

		// then
		assertThatThrownBy(() -> memberService.joinMember(member2))
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
