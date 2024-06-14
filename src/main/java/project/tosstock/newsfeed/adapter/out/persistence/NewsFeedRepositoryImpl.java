package project.tosstock.newsfeed.adapter.out.persistence;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.QFollowEntity;
import project.tosstock.newsfeed.adapter.out.entity.NewsFeedEntity;
import project.tosstock.newsfeed.adapter.out.entity.QNewsFeedEntity;

@RequiredArgsConstructor
public class NewsFeedRepositoryImpl implements NewsFeedRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<NewsFeedEntity> findNewsFeedsJoinFolloweeId(Long memberId) {
		return queryFactory
			.select(QNewsFeedEntity.newsFeedEntity)
			.from(QFollowEntity.followEntity)
			.leftJoin(QNewsFeedEntity.newsFeedEntity)
			.on(QFollowEntity.followEntity.followeeId.eq(QNewsFeedEntity.newsFeedEntity.member.id))
			.where(QFollowEntity.followEntity.followerId.eq(memberId))
			.orderBy(QNewsFeedEntity.newsFeedEntity.createdAt.desc())
			.fetch();
	}
}
