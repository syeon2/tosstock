package project.tosstock.newfeed.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.newfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newfeed.application.domain.model.FeedType;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeedEntity, Long> {

	void deleteByFeedIdAndFeedType(Long feedId, FeedType feedType);
}
