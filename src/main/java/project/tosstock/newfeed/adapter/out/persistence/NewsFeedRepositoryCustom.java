package project.tosstock.newfeed.adapter.out.persistence;

import java.util.List;

import project.tosstock.newfeed.adapter.out.entity.NewsFeedEntity;

public interface NewsFeedRepositoryCustom {

	List<NewsFeedEntity> findNewsFeedsJoinFolloweeId(Long memberId);
}
