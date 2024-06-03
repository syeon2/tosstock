package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

class FollowServiceTest extends IntegrationTestSupport {

	@Autowired
	private FollowService followService;

	@Autowired
	private FollowRepository followRepository;

	@MockBean
	private SaveNewsFeedPort saveNewsFeedPort;

	@MockBean
	private DeleteNewsFeedPort deleteNewsFeedPort;

	@MockBean
	private NewsFeedFilterUseCase newsFeedFilterUseCase;

	@BeforeEach
	void before() {
		followRepository.deleteAllInBatch();
	}

	@Test
	void followMember() {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		// when
		Long savedFollowId = followService.followMember(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(savedFollowId);

		assertThat(findFollowOptional).isPresent()
			.hasValueSatisfying(f -> assertThat(f.getFollowerId()).isEqualTo(followerId))
			.hasValueSatisfying(f -> assertThat(f.getFolloweeId()).isEqualTo(followeeId));
	}

	@Test
	void unfollowMember() {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		followService.followMember(followerId, followeeId);

		// when
		Long deleteFollowId = followService.unfollowMember(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(deleteFollowId);

		assertThat(findFollowOptional).isEmpty();
	}
}
