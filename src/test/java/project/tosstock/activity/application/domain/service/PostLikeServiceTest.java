package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

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
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;

class PostLikeServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostLikeService postLikeService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private StockRepository stockRepository;

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
		stockRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@Transactional
	@DisplayName(value = "회원이 해당 포스트를 좋아합니다. 를 누릅니다.")
	void likePost() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("wwww@wwww"));
		StockEntity savedStock = stockRepository.save(createStock());
		PostEntity savedPost = postRepository.save(createPost(savedMember, savedStock));

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
		StockEntity savedStock = stockRepository.save(createStock());
		PostEntity savedPost = postRepository.save(createPost(savedMember, savedStock));

		Long memberId = savedMember.getId();
		Long postId = savedPost.getId();

		postLikeService.likePost(memberId, postId);

		// when
		boolean result = postLikeService.unlikePost(memberId, postId);

		// then
		assertThat(result).isTrue();

		assertThat(postLikeRepository.findAll()).isEmpty();
	}

	private PostEntity createPost(MemberEntity member, StockEntity stock) {
		return PostEntity.builder()
			.article("hello")
			.member(member)
			.stock(stock)
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

	private StockEntity createStock() {
		return StockEntity.builder()
			.name("hello")
			.market(Market.KOSDOQ)
			.symbol("111")
			.originTime(LocalDateTime.now())
			.build();
	}
}
