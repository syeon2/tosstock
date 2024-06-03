package project.tosstock.newfeed.application.port.in;

import java.util.List;

import project.tosstock.newfeed.application.domain.model.TestNewsFeed;

public interface NewsFeedFilterUseCase {

	List<TestNewsFeed> showNewsFeedBasic(Long memberId);
}
