package project.tosstock.newfeed.adapter.out;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.newfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newfeed.adapter.out.persistence.NewsFeedRepository;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newfeed.application.port.out.FindNewsFeedPort;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class NewsFeedPersistenceAdapter implements SaveNewsFeedPort, DeleteNewsFeedPort, FindNewsFeedPort {

	private final NewsFeedRepository newsFeedRepository;
	private final MemberRepository memberRepository;

	private final NewsFeedMapper newsFeedMapper;

	@Override
	public Long save(NewsFeed newsFeed, FeedType feedType) {
		MemberEntity member = memberRepository.getReferenceById(newsFeed.getMemberId());
		NewsFeedEntity saveNewsFeed = newsFeedRepository.save(newsFeedMapper.toEntity(newsFeed, member, feedType));

		return saveNewsFeed.getId();
	}

	@Override
	@Transactional
	public void delete(Long feedId, FeedType feedType) {
		newsFeedRepository.deleteByFeedIdAndFeedType(feedId, feedType);
	}

	@Override
	public List<NewsFeed> findNewsFeed(Long memberId) {
		return newsFeedRepository.findNewsFeedsJoinFolloweeId(memberId).stream()
			.map(newsFeedMapper::toDomain)
			.collect(Collectors.toList());
	}
}
