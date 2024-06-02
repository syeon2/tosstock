package project.tosstock.newfeed.adapter.out;

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
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class NewsFeedPersistenceAdapter implements SaveNewsFeedPort, DeleteNewsFeedPort {

	private final NewsFeedRepository newsFeedRepository;
	private final MemberRepository memberRepository;

	private final NewsFeedMapper newsFeedMapper;

	@Override
	@Transactional
	public Long save(NewsFeed newsFeed, FeedType feedType) {
		MemberEntity member = memberRepository.findById(newsFeed.getMemberId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		NewsFeedEntity saveNewsFeed = newsFeedRepository.save(newsFeedMapper.toEntity(newsFeed, member, feedType));

		return saveNewsFeed.getId();
	}

	@Override
	@Transactional
	public void delete(Long feedId, FeedType feedType) {
		newsFeedRepository.deleteByFeedIdAndFeedType(feedId, feedType);
	}
}
