package project.tosstock.newfeed.adapter.out;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
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

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private FollowRepository followRepository;

	@BeforeEach
	void before() {
		newsFeedRepository.deleteAllInBatch();
		followRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "뉴스피드를 저장합니다.")
	void save() {
		// given
		MemberEntity savedMember = memberRepository.save(createMember("www@wwww", "00011112222"));

		long feedId = 1L;
		FeedType feedType = FeedType.POST;

		NewsFeed newsFeed = createNewsFeed(feedId, savedMember.getId(), feedType);

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
		MemberEntity savedMember = memberRepository.save(createMember("www@wwww", "00011112222"));

		long feedId = 1L;
		FeedType feedType = FeedType.POST;
		NewsFeed newsFeed = createNewsFeed(feedId, savedMember.getId(), feedType);

		Long savedNewsFeedId = newsFeedPersistenceAdapter.save(newsFeed, feedType);

		// when
		newsFeedPersistenceAdapter.delete(feedId, feedType);

		// then
		Optional<NewsFeedEntity> findNewsFeedOptional = newsFeedRepository.findById(savedNewsFeedId);

		assertThat(findNewsFeedOptional).isEmpty();
	}

	@Test
	@DisplayName(value = "뉴스피드를 조회합니다.")
	void findNewFeed() {
		// given
		MemberEntity savedMember1 = memberRepository.save(createMember("www@wwww", "00011112222"));
		MemberEntity savedMember2 = memberRepository.save(createMember("www@wwww11", "00011112223"));
		MemberEntity savedMember3 = memberRepository.save(createMember("www@wwww112", "00011112111"));

		PostEntity savedPost1 = postRepository.save(createPost(savedMember2, "member2 Post"));
		PostEntity savedPost2 = postRepository.save(createPost(savedMember2, "member2 Post"));
		PostEntity savedPost3 = postRepository.save(createPost(savedMember3, "member3 Post"));

		FollowEntity follow1 = followRepository.save(
			FollowEntity.builder().followerId(savedMember1.getId()).followeeId(savedMember2.getId()).build());

		FollowEntity follow2 = followRepository.save(
			FollowEntity.builder().followerId(savedMember2.getId()).followeeId(savedMember3.getId()).build());

		NewsFeed newsfeed1 = createNewsFeed(savedPost1, savedMember2, "member2 Post");
		NewsFeed newsfeed2 = createNewsFeed(savedPost2, savedMember2, "member2 Post");
		NewsFeed newsfeed3 = createNewsFeed(savedPost3, savedMember3, "member3 Post");
		NewsFeed newsfeed4 = createNewsFeed(follow1, savedMember1);
		NewsFeed newsfeed5 = createNewsFeed(follow2, savedMember2);

		newsFeedPersistenceAdapter.save(newsfeed1, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed2, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed3, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed4, FeedType.FOLLOW);
		newsFeedPersistenceAdapter.save(newsfeed5, FeedType.FOLLOW);

		// when
		List<NewsFeed> findNewsFeeds = newsFeedPersistenceAdapter.findNewsFeed(savedMember1.getId());

		// then  member 1 -> member 2's content (post 2, follow 1)
		assertThat(findNewsFeeds.size()).isEqualTo(3);
	}

	private NewsFeed createNewsFeed(FollowEntity follow1, MemberEntity savedMember1) {
		return NewsFeed.builder()
			.article("member1 follow member2")
			.feedId(follow1.getId())
			.memberId(savedMember1.getId())
			.build();
	}

	private NewsFeed createNewsFeed(PostEntity savedPost1, MemberEntity savedMember2, String article) {
		return NewsFeed.builder()
			.article(article)
			.feedId(savedPost1.getId())
			.memberId(savedMember2.getId())
			.build();
	}

	private NewsFeed createNewsFeed(Long feedId, Long memberId, FeedType feedType) {
		return NewsFeed.builder()
			.feedId(feedId)
			.feedType(feedType)
			.memberId(memberId)
			.article("텍스트 기반 뉴스피드")
			.build();
	}

	private PostEntity createPost(MemberEntity member, String article) {
		return PostEntity.builder()
			.article(article)
			.member(member)
			.build();
	}

	private MemberEntity createMember(String email, String phoneNumber) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber(phoneNumber)
			.build();
	}
}
