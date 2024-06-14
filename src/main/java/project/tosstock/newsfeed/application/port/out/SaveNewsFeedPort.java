package project.tosstock.newsfeed.application.port.out;

import project.tosstock.newsfeed.application.domain.model.FeedType;
import project.tosstock.newsfeed.application.domain.model.NewsFeed;

public interface SaveNewsFeedPort {

	Long save(NewsFeed newsFeed, FeedType feedType);
}
