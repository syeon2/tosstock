package project.tosstock.newsfeed.application.port.out;

import project.tosstock.newsfeed.application.domain.model.FeedType;

public interface DeleteNewsFeedPort {

	void delete(Long feedId, FeedType feedType);
}
