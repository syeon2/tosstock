package project.tosstock.activity.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;

class PostPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private PostPersistenceAdapter postPersistenceAdapter;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	void before() {
		postRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "작성한 포스트를 저장합니다.")
	void save() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());
		StockEntity savedStock = stockRepository.save(createStock());

		String article = "텍스트 작성";
		Post post = createPost(article, savedMember.getId(), savedStock.getId());

		// when
		Long savePostId = postPersistenceAdapter.save(post);

		// then
		Optional<PostEntity> findPostOptional = postRepository.findById(savePostId);

		assertThat(findPostOptional).isPresent()
			.hasValueSatisfying(p -> assertThat(p.getArticle()).isEqualTo(article));
	}

	@Test
	@Transactional
	@DisplayName(value = "회원과 포스트 간 연관관계 설정을 확인합니다.")
	void checkRelatedConnectPostAndMember() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());
		StockEntity savedStock = stockRepository.save(createStock());

		String article = "텍스트 작성";
		Post post = createPost(article, savedMember.getId(), savedStock.getId());

		Long savedPostId = postPersistenceAdapter.save(post);

		// when
		Optional<PostEntity> findPostOptional = postRepository.findById(savedPostId);

		// then
		assertThat(findPostOptional).isPresent()
			.hasValueSatisfying(f -> assertThat(f.getMember().getId()).isEqualTo(savedMember.getId()));
	}

	@Test
	@DisplayName(value = "작성된 포스트를 삭제합니다.")
	void delete() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember());
		StockEntity savedStock = stockRepository.save(createStock());

		String article = "텍스트 작성";
		Post post = createPost(article, savedMember.getId(), savedStock.getId());

		Long savePostId = postPersistenceAdapter.save(post);

		assertThat(postRepository.findById(savePostId)).isPresent()
			.hasValueSatisfying(p -> assertThat(p.getArticle()).isEqualTo(article));

		// when
		postRepository.deleteById(savePostId);

		// then
		assertThat(postRepository.findById(savePostId)).isEmpty();
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
