package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.PostLikeUseCase;
import project.tosstock.activity.application.port.out.DeletePostLikePort;
import project.tosstock.activity.application.port.out.SavePostLikePort;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class PostLikeService implements PostLikeUseCase {

	private final SavePostLikePort savePostLikePort;
	private final DeletePostLikePort deletePostLikePort;

	private final SaveNewsFeedPort saveNewsFeedPort;

	@Override
	@Transactional
	public boolean likePost(Long memberId, Long postId) {
		Long savePostLikeId = savePostLikePort.save(memberId, postId);

		publishNewsFeed(savePostLikeId, memberId);

		return true;
	}

	@Override
	@Transactional
	public boolean unlikePost(Long memberId, Long postId) {
		deletePostLikePort.delete(memberId, postId);

		return true;
	}

	// TODO:: 각 회원들 이름 조회
	private void publishNewsFeed(Long savePostLikeId, Long memberId) {
		NewsFeed newsFeed = NewsFeed.builder()
			.feedId(savePostLikeId)
			.article("A님이 B님의 글을 좋아합니다.")
			.memberId(memberId)
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.POST_LIKE);
	}

}
