package project.tosstock.newsfeed.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.newsfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newsfeed.application.domain.model.FeedType;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeedEntity, Long>, NewsFeedRepositoryCustom {

	void deleteByFeedIdAndFeedType(Long feedId, FeedType feedType);
}
