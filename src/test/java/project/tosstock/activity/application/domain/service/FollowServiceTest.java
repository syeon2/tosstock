package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

class FollowServiceTest extends IntegrationTestSupport {

	@Autowired
	private FollowService followService;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private MemberRepository memberRepository;

	@MockBean
	private SaveNewsFeedPort saveNewsFeedPort;

	@MockBean
	private DeleteNewsFeedPort deleteNewsFeedPort;

	@MockBean
	private NewsFeedFilterUseCase newsFeedFilterUseCase;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	void before() {
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		followRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "회원이 타 회원을 Follow합니다.")
	@Transactional
	void followMember() {
		// given
		MemberEntity member1 = MemberEntity.builder()
			.username("suyeon1")
			.email("waterkite94@gmail.com")
			.password("12345678")
			.phoneNumber("00011112222")
			.build();

		MemberEntity member2 = MemberEntity.builder()
			.username("suyeon1")
			.email("gsy4568@gmail.com")
			.password("12345674")
			.phoneNumber("00011112221")
			.build();

		memberRepository.save(member1);
		memberRepository.save(member2);

		Long followerId = member1.getId();
		Long followeeId = member2.getId();

		// when
		Long savedFollowId = followService.followMember(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(savedFollowId);

		assertThat(findFollowOptional).isPresent()
			.hasValueSatisfying(f -> assertThat(f.getFollowerId()).isEqualTo(followerId))
			.hasValueSatisfying(f -> assertThat(f.getFolloweeId()).isEqualTo(followeeId));
	}

	@Test
	@DisplayName(value = "회원이 타 회원을 언팔로우합니다.")
	@Transactional
	void unfollowMember() {
		// given
		MemberEntity member1 = MemberEntity.builder()
			.username("suyeon1")
			.email("4569ksy@gmail.com")
			.password("12345678")
			.phoneNumber("00011112226")
			.build();

		MemberEntity member2 = MemberEntity.builder()
			.username("suyeon1")
			.email("34568ksy@gmail.com")
			.password("12345674")
			.phoneNumber("00011112228")
			.build();

		memberRepository.save(member1);
		memberRepository.save(member2);

		Long followerId = member1.getId();
		Long followeeId = member2.getId();

		followService.followMember(followerId, followeeId);

		// when
		Long deleteFollowId = followService.unfollowMember(followerId, followeeId);

		// then
		List<FollowEntity> findAll = followRepository.findAll();

		assertThat(findAll).isEmpty();
	}
}
