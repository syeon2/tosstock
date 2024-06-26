package project.tosstock.activity.application.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.CustomPage;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.in.PostingUseCase;
import project.tosstock.activity.application.port.in.SearchPostUseCase;
import project.tosstock.activity.application.port.out.DeletePostPort;
import project.tosstock.activity.application.port.out.FindPostPort;
import project.tosstock.activity.application.port.out.SavePostPort;
import project.tosstock.newsfeed.application.domain.model.FeedType;
import project.tosstock.newsfeed.application.domain.model.NewsFeed;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class PostService implements PostingUseCase, SearchPostUseCase {

	private final SavePostPort savePostPort;
	private final FindPostPort findPostPort;
	private final DeletePostPort deletePostPort;
	private final SaveNewsFeedPort saveNewsFeedPort;

	@Override
	@Transactional
	public Long createPost(Post post) {
		Long savedPostId = savePostPort.save(post);

		publishNewsFeed(post, savedPostId);

		return savedPostId;
	}

	@Override
	@Transactional
	public Long removePost(Long postId) {
		deletePostPort.delete(postId);

		return postId;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MainBoardPostDto> searchPostByArticle(Long memberId, String article, CustomPage page) {
		return findPostPort.findPostByArticleContaining(memberId, article, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MainBoardPostDto> searchPostByStockId(Long memberId, Long stockId, CustomPage page) {
		return findPostPort.findPostByStockId(memberId, stockId, page);
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
