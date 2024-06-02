package project.tosstock.activity.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Comment;

class CommentPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private CommentPersistenceAdapter commentPersistenceAdapter;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	void before() {
		commentRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "댓글을 저장합니다.")
	void save() {
		// given
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(post);

		// when
		Long saveCommentId = commentPersistenceAdapter.save(comment);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isPresent()
			.hasValueSatisfying(c -> assertThat(c.getPost().getId()).isEqualTo(post.getId()));
	}

	@Test
	@DisplayName(value = "댓글을 삭제합니다.")
	void delete() {
		// given
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(post);
		Long saveCommentId = commentPersistenceAdapter.save(comment);

		// when
		commentPersistenceAdapter.delete(saveCommentId);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isEmpty();
	}

	private static Comment createComment(PostEntity post) {
		return Comment.builder()
			.article("댓글")
			.postId(post.getId())
			.build();
	}

	private PostEntity createPost() {
		return PostEntity.builder()
			.article("hello")
			.build();
	}
}
