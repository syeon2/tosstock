package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.in.PostLikeUseCase;
import project.tosstock.activity.application.port.out.DeletePostLikePort;
import project.tosstock.activity.application.port.out.FindPostPort;
import project.tosstock.activity.application.port.out.SavePostLikePort;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.newsfeed.application.domain.model.FeedType;
import project.tosstock.newsfeed.application.domain.model.NewsFeed;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class PostLikeService implements PostLikeUseCase {

	private final SavePostLikePort savePostLikePort;
	private final DeletePostLikePort deletePostLikePort;
	private final FindMemberPort findMemberPort;
	private final FindPostPort findPostPort;

	private final SaveNewsFeedPort saveNewsFeedPort;

	@Override
	@Transactional
	public boolean likePost(Long memberId, Long postId) {
		Long savePostLikeId = savePostLikePort.save(memberId, postId);

		String likePostUsername = findMemberPort.findUsernameById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재히지 않는 회원입니다."));

		Post findPost = findPostPort.findPostById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트입니다."));

		String postedUsername = findMemberPort.findUsernameById(findPost.getMemberId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		publishNewsFeed(savePostLikeId, memberId, likePostUsername, postedUsername);

		return true;
	}

	@Override
	@Transactional
	public boolean unlikePost(Long memberId, Long postId) {
		deletePostLikePort.delete(memberId, postId);

		return true;
	}

	private void publishNewsFeed(Long savePostLikeId, Long memberId, String postLikeUsername, String postedUsername) {
		NewsFeed newsFeed = NewsFeed.builder()
			.feedId(savePostLikeId)
			.article(postLikeUsername + "님이 " + postedUsername + "님의 글을 좋아합니다.")
			.memberId(memberId)
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.POST_LIKE);
	}

}
