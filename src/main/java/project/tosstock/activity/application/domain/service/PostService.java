package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.in.PostingUseCase;
import project.tosstock.activity.application.port.out.DeletePostPort;
import project.tosstock.activity.application.port.out.SavePostPort;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class PostService implements PostingUseCase {

	private final SavePostPort savePostPort;
	private final DeletePostPort deletePostPort;

	private final SaveNewsFeedPort saveNewsFeedPort;
	private final DeleteNewsFeedPort deleteNewsFeedPort;

	@Override
	@Transactional
	public Long createPost(Post post) {
		Long savedPostId = savePostPort.save(post);

		publishNewsFeed(post, savedPostId);

		return savedPostId;
	}

	@Override
	public Long removePost(Long postId) {
		deletePostPort.delete(postId);
		deleteNewsFeedPort.delete(postId, FeedType.POST);

		return postId;
	}

	private void publishNewsFeed(Post post, Long savedPostId) {
		NewsFeed newsFeed = NewsFeed.builder()
			.article(post.getArticle())
			.feedId(savedPostId)
			.memberId(post.getMemberId())
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.POST);
	}
}
