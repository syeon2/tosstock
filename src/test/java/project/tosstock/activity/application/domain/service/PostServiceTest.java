package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

class PostServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void before() {
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "게시글을 작성합니다.")
	void create_post() {
		// given
		MemberEntity member = createMember();
		memberRepository.save(member);

		// when
		Post post = createPost("텍스트 입니다.", member.getId());
		Long createPostId = postService.createPost(post);

		// then
		Optional<PostEntity> findPostOptional = postRepository.findById(createPostId);

		assertThat(findPostOptional).isPresent()
			.hasValueSatisfying(p -> assertThat(p.getMember().getId()).isEqualTo(member.getId()));
	}

	@Test
	@DisplayName(value = "게시글을 삭제합니다.")
	void remove_post() {
		// given
		MemberEntity member = createMember();
		memberRepository.save(member);

		Post post = createPost("텍스트 입니다.", member.getId());
		Long createPostId = postService.createPost(post);

		// when
		postService.removePost(createPostId);

		// then
		Optional<PostEntity> findPostOptional = postRepository.findById(createPostId);

		assertThat(findPostOptional).isEmpty();
	}

	private MemberEntity createMember() {
		return MemberEntity.builder()
			.username("suyeon")
			.email("www@wwww")
			.password("12345678")
			.phoneNumber("00011112222")
			.build();
	}

	private Post createPost(String article, Long memberId) {
		return Post.builder()
			.article(article)
			.memberId(memberId)
			.build();
	}
}
