package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newsfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newsfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

class PostLikeServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostLikeService postLikeService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@MockBean
	private NewsFeedFilterUseCase newsFeedFilterUseCase;

	@MockBean
	private SaveNewsFeedPort saveNewsFeedPort;

	@MockBean
	private DeleteNewsFeedPort deleteNewsFeedPort;

	@BeforeEach
	void before() {
		postLikeRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@Transactional
	@DisplayName(value = "회원이 해당 포스트를 좋아합니다. 를 누릅니다.")
	void likePost() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("wwww@wwww"));
		PostEntity savedPost = postRepository.save(createPost(savedMember));

		// when
		Long memberId = savedMember.getId();
		Long postId = savedPost.getId();

		boolean result = postLikeService.likePost(memberId, postId);

		// then
		assertThat(result).isTrue();

		assertThat(postLikeRepository.findAll().get(0).getPost().getId()).isEqualTo(postId);
		assertThat(postLikeRepository.findAll().get(0).getMember().getId()).isEqualTo(memberId);
	}

	@Test
	@Transactional
	@DisplayName(value = "회원이 해당 포스트를 좋아합니다. 를 해제합니다.")
	void unlike_post() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("wwww@wwww"));
		PostEntity savedPost = postRepository.save(createPost(savedMember));

		Long memberId = savedMember.getId();
		Long postId = savedPost.getId();

		postLikeService.likePost(memberId, postId);

		// when
		boolean result = postLikeService.unlikePost(memberId, postId);

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

	private MemberEntity createMember(String email) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber("01000001111")
			.build();
	}
}
