package project.tosstock.newfeed.application.port.out;

import project.tosstock.newfeed.application.domain.model.FeedType;

public interface DeleteNewsFeedPort {

	void delete(Long feedId, FeedType feedType);
}
