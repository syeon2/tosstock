package project.tosstock.activity.application.domain.service;

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

class CommentServiceTest extends IntegrationTestSupport {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	void before() {
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "댓글을 생성합니다.")
	void create_comment() {
		// given
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(post);

		// when
		Long saveCommentId = commentService.createComment(comment);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isPresent()
			.hasValueSatisfying(c -> assertThat(c.getPost().getId()).isEqualTo(post.getId()));
	}

	@Test
	@DisplayName(value = "댓글을 삭제합니다.")
	void remove_comment() {
		// given
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(post);
		Long saveCommentId = commentService.createComment(comment);

		// when
		Long removedCommentId = commentService.removeComment(saveCommentId);

		// then
		assertThat(commentRepository.findById(removedCommentId)).isEmpty();
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
