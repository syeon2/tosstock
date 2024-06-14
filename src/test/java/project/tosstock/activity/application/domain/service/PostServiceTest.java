package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newsfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.FindNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

class PostServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@MockBean
	private SaveNewsFeedPort saveNewsFeedPort;

	@MockBean
	private DeleteNewsFeedPort deleteNewsFeedPort;

	@MockBean
	private FindNewsFeedPort findNewsFeedPort;

	@BeforeEach
	void before() {
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "게시글을 작성합니다.")
	void createPost() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());

		// when
		Post post = createPost("텍스트 입니다.", savedMember.getId());
		Long createPostId = postService.createPost(post);

		// then
		Optional<PostEntity> findPostOptional = postRepository.findById(createPostId);

		assertThat(findPostOptional).isPresent()
			.hasValueSatisfying(p -> assertThat(p.getMember().getId()).isEqualTo(savedMember.getId()));
	}

	@Test
	@DisplayName(value = "게시글을 삭제합니다.")
	void removePost() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());

		Post post = createPost("텍스트 입니다.", savedMember.getId());
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
