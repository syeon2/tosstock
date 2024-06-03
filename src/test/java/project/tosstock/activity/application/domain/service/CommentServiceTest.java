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
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newfeed.adapter.out.persistence.NewsFeedRepository;

class CommentServiceTest extends IntegrationTestSupport {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private NewsFeedRepository newsFeedRepository;

	@BeforeEach
	void before() {
		newsFeedRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "댓글을 생성합니다.")
	void create_comment() {
		// given
		MemberEntity member = createMemberEntity("www@www");
		memberRepository.save(member);
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(member, post);

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
		MemberEntity member = createMemberEntity("www@www");
		memberRepository.save(member);
		PostEntity post = createPost();
		postRepository.save(post);

		Comment comment = createComment(member, post);

		Long saveCommentId = commentService.createComment(comment);

		// when
		Long removedCommentId = commentService.removeComment(saveCommentId);

		// then
		assertThat(commentRepository.findById(removedCommentId)).isEmpty();
	}

	private MemberEntity createMemberEntity(String email) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber("01000001111")
			.introduce("hellO")
			.profileImageUrl("")
			.build();
	}

	private static Comment createComment(MemberEntity member, PostEntity post) {
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
