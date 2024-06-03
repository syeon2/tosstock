package project.tosstock.newfeed.application.port.out;

import java.util.List;

import project.tosstock.newfeed.application.domain.model.NewsFeed;

public interface FindNewsFeedPort {

	List<NewsFeed> findNewsFeed(Long memberId);
}
