package project.tosstock.activity.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.entity.PostLikeEntity;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

class PostLikePersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private PostLikePersistenceAdapter postLikePersistenceAdapter;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@BeforeEach
	void before() {
		postLikeRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "게시글에 좋아요를 저장합니다.")
	void save() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@wwww"));
		PostEntity savedPost = postRepository.save(createPost(savedMember));

		// when
		Long memberId = savedMember.getId();
		Long postId = savedPost.getId();

		Long savedPostLikeId = postLikePersistenceAdapter.save(memberId, postId);

		// then
		Optional<PostLikeEntity> findPostLikeOptional = postLikeRepository.findById(savedPostLikeId);

		assertThat(findPostLikeOptional).isPresent()
			.hasValueSatisfying(p -> assertThat(p.getPost().getId()).isEqualTo(memberId))
			.hasValueSatisfying(p -> assertThat(p.getMember().getId()).isEqualTo(postId));
	}

	@Test
	@DisplayName(value = "게시글에 좋아요를 삭제합니다.")
	void delete() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@wwww"));
		PostEntity savedPost = postRepository.save(createPost(savedMember));

		Long memberId = savedMember.getId();
		Long postId = savedPost.getId();

		Long savedPostLikeId = postLikePersistenceAdapter.save(memberId, postId);

		// when
		postLikePersistenceAdapter.delete(memberId, postId);
		
		// then
		Optional<PostLikeEntity> findPostLikeOptional = postLikeRepository.findById(savedPostLikeId);

		assertThat(findPostLikeOptional).isEmpty();
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
			.introduce("hellO")
			.profileImageUrl("")
			.build();
	}
}
