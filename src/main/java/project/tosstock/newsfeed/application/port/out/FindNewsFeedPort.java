package project.tosstock.newsfeed.application.port.out;

import java.util.List;

import project.tosstock.newsfeed.application.domain.model.NewsFeed;

public interface FindNewsFeedPort {

	List<NewsFeed> findNewsFeed(Long memberId);
}
