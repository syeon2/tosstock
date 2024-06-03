package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.activity.application.port.out.DeleteFollowPort;
import project.tosstock.activity.application.port.out.SaveFollowPort;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowMemberUseCase {

	private final SaveFollowPort saveFollowPort;
	private final DeleteFollowPort deleteFollowPort;

	private final SaveNewsFeedPort saveNewsFeedPort;

	@Override
	public Long followMember(Long followerId, Long followeeId) {
		Long savedFollowId = saveFollowPort.save(followerId, followeeId);

		publishNewsFeed(followerId, savedFollowId);

		return savedFollowId;
	}

	@Override
	public Long unfollowMember(Long followerId, Long followeeId) {
		deleteFollowPort.delete(followerId, followeeId);

		return followerId;
	}

	private void publishNewsFeed(Long followerId, Long savedFollowId) {
		NewsFeed newsFeed = NewsFeed.builder()
			.article("A가 B를 팔로우합니다.")
			.feedId(savedFollowId)
			.memberId(followerId)
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.POST);
	}
}
