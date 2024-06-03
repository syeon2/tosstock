package project.tosstock.activity.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;

class FollowServiceTest extends IntegrationTestSupport {

	@Autowired
	private FollowService followService;

	@Autowired
	private FollowRepository followRepository;

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
		followService.followMember(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(
			FollowEntity.PK.builder().followerId(followerId).followeeId(followeeId).build());

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
		boolean result = followService.unfollowMember(followerId, followeeId);

		// then
		assertThat(result).isTrue();

		Optional<FollowEntity> findFollowOptional = followRepository.findById(
			FollowEntity.PK.builder().followerId(followerId).followeeId(followeeId).build());

		assertThat(findFollowOptional).isEmpty();
	}
}