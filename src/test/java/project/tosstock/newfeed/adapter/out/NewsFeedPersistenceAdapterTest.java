package project.tosstock.newfeed.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newfeed.adapter.out.persistence.NewsFeedRepository;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;

class NewsFeedPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private NewsFeedPersistenceAdapter newsFeedPersistenceAdapter;

	@Autowired
	private NewsFeedRepository newsFeedRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void before() {
		newsFeedRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "뉴스피드를 저장합니다.")
	void save() {
		// given
		MemberEntity member = createMemberEntity();
		memberRepository.save(member);

		long feedId = 1L;
		FeedType feedType = FeedType.POST;
		NewsFeed newsFeed = createNewsFeed(feedId, member.getId(), feedType);

		// when
		Long savedNewsFeedId = newsFeedPersistenceAdapter.save(newsFeed, feedType);

		// then
		Optional<NewsFeedEntity> findNewsFeedOptional = newsFeedRepository.findById(savedNewsFeedId);

		assertThat(findNewsFeedOptional).isPresent()
			.hasValueSatisfying(n -> assertThat(n.getId()).isEqualTo(savedNewsFeedId))
			.hasValueSatisfying(n -> assertThat(n.getFeedId()).isEqualTo(feedId))
			.hasValueSatisfying(n -> assertThat(n.getFeedType()).isEqualTo(feedType));
	}

	@Test
	@DisplayName(value = "뉴스피드를 삭제합니다.")
	void delete() {
		// given
		MemberEntity member = createMemberEntity();
		memberRepository.save(member);

		long feedId = 1L;
		FeedType feedType = FeedType.POST;
		NewsFeed newsFeed = createNewsFeed(feedId, member.getId(), feedType);

		Long savedNewsFeedId = newsFeedPersistenceAdapter.save(newsFeed, feedType);

		// when
		newsFeedPersistenceAdapter.delete(feedId, feedType);

		// then
		Optional<NewsFeedEntity> findNewsFeedOptional = newsFeedRepository.findById(savedNewsFeedId);

		assertThat(findNewsFeedOptional).isEmpty();
	}

	private NewsFeed createNewsFeed(Long feedId, Long memberId, FeedType feedType) {
		return NewsFeed.builder()
			.feedId(feedId)
			.feedType(feedType)
			.memberId(memberId)
			.article("텍스트 기반 뉴스피드")
			.build();
	}

	private MemberEntity createMemberEntity() {
		return MemberEntity.builder()
			.username("suyeon")
			.email("www@wwww")
			.password("12345678")
			.phoneNumber("00011112222")
			.build();
	}
}
