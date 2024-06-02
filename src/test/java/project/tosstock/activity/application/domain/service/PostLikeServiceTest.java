package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

class PostLikeServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostLikeService postLikeService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@BeforeEach
	void before() {
		postLikeRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@Transactional
	@DisplayName(value = "회원이 해당 포스트를 좋아합니다. 를 누릅니다.")
	void like_post() {
		// given
		MemberEntity member = createMemberEntity("wwww@wwww");
		memberRepository.save(member);

		PostEntity post = createPost(member);
		postRepository.save(post);

		// when
		boolean result = postLikeService.likePost(member.getId(), post.getId());

		// then
		assertThat(result).isTrue();

		assertThat(postLikeRepository.findAll().get(0).getPost().getId()).isEqualTo(post.getId());
		assertThat(postLikeRepository.findAll().get(0).getMember().getId()).isEqualTo(member.getId());
	}

	@Test
	@Transactional
	@DisplayName(value = "회원이 해당 포스트를 좋아합니다. 를 해제합니다.")
	void unlike_post() {
		// given
		MemberEntity member = createMemberEntity("wwww@wwww");
		memberRepository.save(member);

		PostEntity post = createPost(member);
		postRepository.save(post);

		postLikeService.likePost(member.getId(), post.getId());

		// when
		boolean result = postLikeService.unlikePost(member.getId(), post.getId());

		// then
		assertThat(result).isTrue();

		assertThat(postLikeRepository.findAll()).isEmpty();
	}

	private PostEntity createPost(MemberEntity member) {
		return PostEntity.builder()
			.article("hello")
			.member(member)
			.build();
	}

	private MemberEntity createMemberEntity(String email) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber("01000001111")
			.build();
	}
}
