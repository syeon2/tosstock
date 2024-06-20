package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newsfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.FindNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;

class PostServiceTest extends IntegrationTestSupport {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private StockRepository stockRepository;

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
		StockEntity savedStock = stockRepository.save(createStock());

		// when
		Post post = createPost("텍스트 입니다.", savedMember.getId(), savedStock.getId());
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
		StockEntity savedStock = stockRepository.save(createStock());

		Post post = createPost("텍스트 입니다.", savedMember.getId(), savedStock.getId());
		Long createPostId = postService.createPost(post);

		// when
		postService.removePost(createPostId);

		// then
		Optional<PostEntity> findPostOptional = postRepository.findById(createPostId);

		assertThat(findPostOptional).isEmpty();
	}

	@Test
	@DisplayName(value = "검색 기록을 통해 게시글을 검색합니다.")
	void searchPostByArticle() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());
		StockEntity savedStock = stockRepository.save(createStock());

		String article = "텍스트 입니다.";

		Post post = createPost(article + "testing", savedMember.getId(), savedStock.getId());
		Long createPostId = postService.createPost(post);

		// when

		PageRequest pageable = PageRequest.of(0, 10);
		List<MainBoardPostDto> findPosts = postService.searchPostByArticle(article, pageable);

		// then
		assertThat(findPosts.size()).isEqualTo(1);
	}

	@Test
	@DisplayName(value = "증권 종목 아이디를 통해 게시글을 검색합니다.")
	void searchPostByStockId() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());
		StockEntity savedStock = stockRepository.save(createStock());

		Post post1 = createPost("testing", savedMember.getId(), savedStock.getId());
		Post post2 = createPost("testing", savedMember.getId(), savedStock.getId());
		Long savedPost1 = postService.createPost(post1);
		Long savedPost2 = postService.createPost(post2);

		// when

		PageRequest pageable = PageRequest.of(0, 10);
		List<Post> findPosts = postService.searchPostByStockId(savedStock.getId(), pageable);

		// then
		assertThat(findPosts).hasSize(2)
			.extracting("id")
			.containsExactlyInAnyOrder(savedPost1, savedPost2);
	}

	private MemberEntity createMember() {
		return MemberEntity.builder()
			.username("suyeon")
			.email("www@wwww")
			.password("12345678")
			.phoneNumber("00011112222")
			.build();
	}

	private Post createPost(String article, Long memberId, Long stockId) {
		return Post.builder()
			.article(article)
			.memberId(memberId)
			.stockId(stockId)
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
