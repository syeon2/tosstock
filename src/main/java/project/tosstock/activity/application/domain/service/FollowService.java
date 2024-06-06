package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.activity.application.port.out.DeleteFollowPort;
import project.tosstock.activity.application.port.out.SaveFollowPort;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowMemberUseCase {

	private final SaveFollowPort saveFollowPort;
	private final DeleteFollowPort deleteFollowPort;
	private final FindMemberPort findMemberPort;

	private final SaveNewsFeedPort saveNewsFeedPort;

	@Override
	public Long followMember(Long followerId, Long followeeId) {
		Long savedFollowId = saveFollowPort.save(followerId, followeeId);

		// MemberEntity findFollower = findMemberPort.findMemberById(followerId)
		// 	.orElseThrow(() -> new IllegalArgumentException("존재히지 않는 회원입니다."));
		//
		// MemberEntity findFollowee = findMemberPort.findMemberById(followeeId)
		// 	.orElseThrow(() -> new IllegalArgumentException("존재히지 않는 회원입니다."));

		// publishNewsFeed(followerId, savedFollowId, findFollower.getUsername(), findFollowee.getUsername());

		return savedFollowId;
	}

	@Override
	public Long unfollowMember(Long followerId, Long followeeId) {
		deleteFollowPort.delete(followerId, followeeId);

		return followerId;
	}

	private void publishNewsFeed(Long followerId, Long savedFollowId, String followerUsername,
		String followeeUsername) {
		NewsFeed newsFeed = NewsFeed.builder()
			.article(followerUsername + "가 " + followeeUsername + "를 팔로우합니다.")
			.feedId(savedFollowId)
			.memberId(followerId)
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.POST);
	}
}
