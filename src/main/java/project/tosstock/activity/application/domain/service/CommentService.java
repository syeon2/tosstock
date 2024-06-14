package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.activity.application.port.in.CommentUseCase;
import project.tosstock.activity.application.port.out.DeleteCommentPort;
import project.tosstock.activity.application.port.out.SaveCommentPort;
import project.tosstock.newsfeed.application.domain.model.FeedType;
import project.tosstock.newsfeed.application.domain.model.NewsFeed;
import project.tosstock.newsfeed.application.port.out.DeleteNewsFeedPort;
import project.tosstock.newsfeed.application.port.out.SaveNewsFeedPort;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final SaveCommentPort saveCommentPort;
	private final DeleteCommentPort deleteCommentPort;

	private final SaveNewsFeedPort saveNewsFeedPort;
	private final DeleteNewsFeedPort deleteNewsFeedPort;

	@Override
	@Transactional
	public Long createComment(Comment comment) {
		Long savedCommentId = saveCommentPort.save(comment);

		publishNewsFeed(comment, savedCommentId);
		return savedCommentId;
	}

	@Override
	@Transactional
	public Long removeComment(Long commentId) {
		Long deletedCommentId = deleteCommentPort.delete(commentId);

		deleteNewsFeedPort.delete(deletedCommentId, FeedType.COMMENT);

		return deletedCommentId;
	}

	private void publishNewsFeed(Comment comment, Long savedCommentId) {
		NewsFeed newsFeed = NewsFeed.builder()
			.article(comment.getArticle())
			.feedId(savedCommentId)
			.memberId(comment.getMemberId())
			.build();

		saveNewsFeedPort.save(newsFeed, FeedType.COMMENT);
	}
}
