package project.tosstock.newfeed.application.port.out;

import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;

public interface SaveNewsFeedPort {

	Long save(NewsFeed newsFeed, FeedType feedType);
}
