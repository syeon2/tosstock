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
			.article(newsFeed.getArticle())
			.member(member)
			.build();
	}

	public NewsFeed toDomain(NewsFeedEntity newsFeed) {
		return NewsFeed.builder()
			.id(newsFeed.getId())
			.feedId(newsFeed.getFeedId())
			.feedType(newsFeed.getFeedType())
			.memberId(newsFeed.getMember().getId())
			.article(newsFeed.getArticle())
			.build();
	}
}
