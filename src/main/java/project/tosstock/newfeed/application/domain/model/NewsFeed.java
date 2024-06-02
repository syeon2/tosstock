package project.tosstock.newfeed.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsFeed {

	private Long id;
	private Long feedId;
	private FeedType feedType;
	private Long memberId;

	@Builder
	private NewsFeed(Long id, Long feedId, FeedType feedType, Long memberId) {
		this.id = id;
		this.feedId = feedId;
		this.feedType = feedType;
		this.memberId = memberId;
	}
}
