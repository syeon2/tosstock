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

		// when
		Long savedFollowId = followPersistenceAdapter.save(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(savedFollowId);

		assertThat(findFollowOptional).isPresent()
			.hasValueSatisfying(f -> assertThat(f.getFollowerId()).isEqualTo(followerId))
			.hasValueSatisfying(f -> assertThat(f.getFolloweeId()).isEqualTo(followeeId));
	}

	@Test
	@DisplayName(value = "follow 관계를 삭제합니다.")
	void delete() {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		Long savedFollowId = followPersistenceAdapter.save(followerId, followeeId);

		// when
		followPersistenceAdapter.delete(followerId, followeeId);

		// then
		Optional<FollowEntity> findFollowOptional = followRepository.findById(savedFollowId);

		assertThat(findFollowOptional).isEmpty();
	}
}
