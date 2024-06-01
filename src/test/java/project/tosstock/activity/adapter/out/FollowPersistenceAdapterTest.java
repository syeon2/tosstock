package project.tosstock.activity.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.application.domain.model.Follow;

class FollowPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private FollowPersistenceAdapter followPersistenceAdapter;

	@Autowired
	private FollowRepository followRepository;

	@BeforeEach
	void beforeEach() {
		followRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "follow 관계를 저장합니다.")
	void save() {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		Follow follow = Follow.builder()
			.followerId(followerId)
			.followeeId(followeeId)
			.build();

		// when
		followPersistenceAdapter.save(follow);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(
			FollowEntity.PK.builder().followerId(followerId).followeeId(followeeId).build());

		assertThat(findFollowOptional).isPresent()
			.hasValueSatisfying(f -> assertThat(f.getFollowerId()).isEqualTo(followerId))
			.hasValueSatisfying(f -> assertThat(f.getFolloweeId()).isEqualTo(followeeId));
	}
}
