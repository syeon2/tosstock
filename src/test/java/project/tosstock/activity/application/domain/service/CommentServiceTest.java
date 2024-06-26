package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newsfeed.adapter.out.persistence.NewsFeedRepository;
import project.tosstock.newsfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.FindNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

class CommentServiceTest extends IntegrationTestSupport {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

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

	@Autowired
	private NewsFeedRepository newsFeedRepository;

	@BeforeEach
	void after() {
		newsFeedRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "댓글을 생성합니다.")
	void createComment() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedMember, savedPost);

		// when
		Long saveCommentId = commentService.createComment(comment);

		// then
		Optional<CommentEntity> findCommentOptional = commentRepository.findById(saveCommentId);

		assertThat(findCommentOptional).isPresent()
			.hasValueSatisfying(c -> assertThat(c.getPost().getId()).isEqualTo(savedPost.getId()))
			.hasValueSatisfying(c -> assertThat(c.getMember().getId()).isEqualTo(savedMember.getId()));
	}

	@Test
	@DisplayName(value = "댓글을 삭제합니다.")
	void remove_comment() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedMember, savedPost);

		Long saveCommentId = commentService.createComment(comment);

		// when
		Long removedCommentId = commentService.removeComment(saveCommentId);

		// then
		assertThat(commentRepository.findById(removedCommentId)).isEmpty();
	}

	@Test
	@DisplayName(value = "게시글에 대한 댓글을 조회합니다.")
	void fetchPostComments() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@www"));
		PostEntity savedPost = postRepository.save(createPost());

		Comment comment = createComment(savedMember, savedPost);

		Long saveCommentId = commentService.createComment(comment);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Comment> findComments = commentService.fetchPostComments(savedPost.getId(), pageable);

		// then
		assertThat(findComments).hasSize(1);
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
