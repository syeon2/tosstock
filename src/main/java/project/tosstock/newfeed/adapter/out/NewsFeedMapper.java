package project.tosstock.newfeed.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.newfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.NewsFeed;

@Component
public class NewsFeedMapper {

	public NewsFeedEntity toEntity(NewsFeed newsFeed, MemberEntity member, FeedType feedType) {
		return NewsFeedEntity.builder()
			.feedId(newsFeed.getFeedId())
			.feedType(feedType)
			.member(member)
			.build();
	}
}
