package project.tosstock.newfeed.application.port.in;

import java.util.List;

import project.tosstock.newfeed.application.domain.model.NewsFeed;

public interface NewsFeedFilterUseCase {

	List<NewsFeed> showNewsFeedBasic(Long memberId);
}
