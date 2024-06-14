package project.tosstock.newsfeed.application.port.in;

import java.util.List;

import project.tosstock.newsfeed.application.domain.model.TestNewsFeed;

public interface NewsFeedFilterUseCase {

	List<TestNewsFeed> showNewsFeedBasic(Long memberId);
}
