package project.tosstock.newfeed.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TestNewsFeed {

	private Long id;
	private Long feedId;
	private FeedType feedType;
	private String username;
	private String article;

	@Builder
	private TestNewsFeed(Long id, Long feedId, FeedType feedType, String username, String article) {
		this.id = id;
		this.feedId = feedId;
		this.feedType = feedType;
		this.username = username;
		this.article = article;
	}

	public static TestNewsFeed toDomain(NewsFeed newsFeed, String username) {
		return TestNewsFeed.builder()
			.id(newsFeed.getId())
			.feedId(newsFeed.getFeedId())
			.username(username)
			.feedType(newsFeed.getFeedType())
			.article(newsFeed.getArticle())
			.build();
	}
}
