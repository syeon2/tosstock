package project.tosstock.member.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.EncryptedPassword;
import project.tosstock.member.adapter.out.entity.MemberEntity;

class MemberRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void before() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "이메일을 통해 회원을 조회합니다.")
	void find_member_by_email() {
		// given
		String email = "waterkite94@gmail.com";
		MemberEntity entity = createMemberEntity(email);

		memberRepository.save(entity);

		// when
		Optional<MemberEntity> findMemberByEmail = memberRepository.findByEmail(email);

		// then
		assertThat(findMemberByEmail).isPresent()
			.hasValueSatisfying(e -> assertThat(e.getEmail()).isEqualTo(email));
	}

	@Test
	@DisplayName(value = "중복되는 이메일이 존재한다면 예외를 반환합니다.")
	void find_member_by_email_duplicate() {
		// given
		String email = "waterkite94@gmail.com";

		MemberEntity entity1 = createMemberEntity(email);
		MemberEntity entity2 = createMemberEntity(email);

		memberRepository.save(entity1);

		// when // then
		assertThatThrownBy(() -> memberRepository.save(entity2))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	private MemberEntity createMemberEntity(String email) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.encryptedPassword(EncryptedPassword.builder()
				.password("123456778")
				.salt("salt")
				.build()
			)
			.phoneNumber("01000001111")
			.introduce("hellO")
			.profileImageUrl("")
			.build();
	}
}
