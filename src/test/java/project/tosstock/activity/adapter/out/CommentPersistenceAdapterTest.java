package project.tosstock.activity.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.stock.adpater.out.persistence.StockRepository;

class CommentPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private CommentPersistenceAdapter commentPersistenceAdapter;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void before() {
		stockRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "댓글을 저장합니다.")
	void save() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedPost, savedMember);

		// when
		Long saveCommentId = commentPersistenceAdapter.save(comment);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isPresent()
			.hasValueSatisfying(c -> assertThat(c.getPost().getId()).isEqualTo(savedPost.getId()))
			.hasValueSatisfying(c -> assertThat(c.getMember().getId()).isEqualTo(savedMember.getId()));
	}

	@Test
	@DisplayName(value = "댓글을 삭제합니다.")
	void delete() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedPost, savedMember);

		Long saveCommentId = commentPersistenceAdapter.save(comment);

		// when
		commentPersistenceAdapter.delete(saveCommentId);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isEmpty();
	}

	@Test
	@DisplayName(value = "특정 포스트 아이디를 가진 댓글들 조회합니다.")
	void findCommentByPostId() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedPost, savedMember);
		commentPersistenceAdapter.save(comment);
		commentPersistenceAdapter.save(comment);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Comment> findComments = commentPersistenceAdapter.findCommentByPostId(savedPost.getId(), pageable);

		// then
		assertThat(findComments).hasSize(2);
	}

	private MemberEntity createMember(String email) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber("01000001111")
			.introduce("hellO")
			.profileImageUrl("")
			.build();
	}

	private static Comment createComment(PostEntity post, MemberEntity member) {
		return Comment.builder()
			.article("댓글")
			.memberId(member.getId())
			.postId(post.getId())
			.build();
	}

	private PostEntity createPost() {
		return PostEntity.builder()
			.article("hello")
			.build();
	}
}
