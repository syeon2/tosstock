package project.tosstock.newsfeed.adapter.out.persistence;

import java.util.List;

import project.tosstock.newsfeed.adapter.out.entity.NewsFeedEntity;

public interface NewsFeedRepositoryCustom {

	List<NewsFeedEntity> findNewsFeedsJoinFolloweeId(Long memberId);
}
