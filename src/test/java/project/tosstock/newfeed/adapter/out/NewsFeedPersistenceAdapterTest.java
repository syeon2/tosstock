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
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
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

	@Autowired
	private PostLikeRepository postLikeRepository;

	@BeforeEach
	void before() {
		newsFeedRepository.deleteAllInBatch();
		postLikeRepository.deleteAllInBatch();
		followRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "뉴스피드를 저장합니다.")
	void save() {
		// given
		MemberEntity member = createMemberEntity("www@wwww", "00011112222");
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
		MemberEntity member = createMemberEntity("www@wwww", "00011112222");
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

	@Test
	@DisplayName(value = "뉴스피드를 조회합니다.")
	void findNewFeed() {
		// given
		MemberEntity member1 = createMemberEntity("www@wwww", "00011112222");
		memberRepository.save(member1);
		MemberEntity member2 = createMemberEntity("www@wwww11", "00011112223");
		memberRepository.save(member2);
		MemberEntity member3 = createMemberEntity("www@wwww112", "00011112111");
		memberRepository.save(member3);

		PostEntity post = createPost(member2, "member2 Post");
		postRepository.save(post);
		PostEntity post2 = createPost(member2, "member2 Post");
		postRepository.save(post2);
		PostEntity post3 = createPost(member3, "member3 Post");
		postRepository.save(post3);

		FollowEntity follow1 = followRepository.save(
			FollowEntity.builder().followerId(member1.getId()).followeeId(member2.getId()).build());

		FollowEntity follow2 = followRepository.save(
			FollowEntity.builder().followerId(member2.getId()).followeeId(member3.getId()).build());

		NewsFeed newsfeed1 = NewsFeed.builder()
			.article("member2 Post")
			.feedId(post.getId())
			.memberId(member2.getId())
			.build();

		NewsFeed newsfeed2 = NewsFeed.builder()
			.article("member2 Post")
			.feedId(post2.getId())
			.memberId(member2.getId())
			.build();

		NewsFeed newsfeed3 = NewsFeed.builder()
			.article("member3 Post")
			.feedId(post3.getId())
			.memberId(member3.getId())
			.build();

		NewsFeed newsfeed4 = NewsFeed.builder()
			.article("member1 follow member2")
			.feedId(follow1.getId())
			.memberId(member1.getId())
			.build();

		NewsFeed newsfeed5 = NewsFeed.builder()
			.article("member2 follow member3")
			.feedId(follow2.getId())
			.memberId(member2.getId())
			.build();

		newsFeedPersistenceAdapter.save(newsfeed1, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed2, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed3, FeedType.POST);
		newsFeedPersistenceAdapter.save(newsfeed4, FeedType.FOLLOW);
		newsFeedPersistenceAdapter.save(newsfeed5, FeedType.FOLLOW);

		// when
		List<NewsFeed> findNewsFeeds = newsFeedPersistenceAdapter.findNewsFeed(member1.getId());

		// then  member 1 -> member 2's content (post 2, follow 1)
		assertThat(findNewsFeeds.size()).isEqualTo(3);
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

	private MemberEntity createMemberEntity(String email, String phoneNumber) {
		return MemberEntity.builder()
			.username("suyeon")
			.email(email)
			.password("12345678")
			.phoneNumber(phoneNumber)
			.build();
	}
}
