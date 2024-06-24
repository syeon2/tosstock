package project.tosstock.activity.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.QCommentEntity;
import project.tosstock.activity.adapter.out.entity.QPostEntity;
import project.tosstock.activity.adapter.out.entity.QPostLikeEntity;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.MemberDto;
import project.tosstock.member.adapter.out.entity.QMemberEntity;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MainBoardPostDto> findMainBoardPostDtoByArticle(String article, Long offset, Long limit, String sort) {
		return queryFactory
			.select(Projections.constructor(MainBoardPostDto.class,
				QPostEntity.postEntity.id,
				Projections.constructor(MemberDto.class,
					QMemberEntity.memberEntity.id,
					QMemberEntity.memberEntity.username
				),
				QPostEntity.postEntity.article,
				QPostLikeEntity.postLikeEntity.count().intValue(),
				QCommentEntity.commentEntity.count().intValue(),
				QPostEntity.postEntity.createdAt,
				QPostEntity.postEntity.updatedAt
			))
			.from(QPostEntity.postEntity)
			.join(QPostEntity.postEntity.member, QMemberEntity.memberEntity)
			.leftJoin(QPostLikeEntity.postLikeEntity)
			.on(QPostLikeEntity.postLikeEntity.post.id.eq(QPostEntity.postEntity.id))
			.leftJoin(QCommentEntity.commentEntity)
			.on(QCommentEntity.commentEntity.post.id.eq(QPostEntity.postEntity.id))
			.where(QPostEntity.postEntity.article.contains(article))
			.where((sort.equalsIgnoreCase("desc") ? QPostEntity.postEntity.id.lt(offset) :
				QPostEntity.postEntity.id.gt(offset)))
			.groupBy(QPostEntity.postEntity.id, QMemberEntity.memberEntity.id, QMemberEntity.memberEntity.username,
				QPostEntity.postEntity.article, QPostEntity.postEntity.createdAt, QPostEntity.postEntity.updatedAt)
			.orderBy((sort.equalsIgnoreCase("desc") ? QPostEntity.postEntity.createdAt.desc() :
				QPostEntity.postEntity.createdAt.asc()))
			.limit(limit)
			.fetch();
	}

	@Override
	public List<MainBoardPostDto> findMainBoardPostDtoByStockId(Long stockId, Long offset, Long limit, String sort) {
		return queryFactory
			.select(Projections.constructor(MainBoardPostDto.class,
				QPostEntity.postEntity.id,
				Projections.constructor(MemberDto.class,
					QMemberEntity.memberEntity.id,
					QMemberEntity.memberEntity.username
				),
				QPostEntity.postEntity.article,
				QPostLikeEntity.postLikeEntity.count().intValue(),
				QCommentEntity.commentEntity.count().intValue(),
				QPostEntity.postEntity.createdAt,
				QPostEntity.postEntity.updatedAt
			))
			.from(QPostEntity.postEntity)
			.join(QPostEntity.postEntity.member, QMemberEntity.memberEntity)
			.leftJoin(QPostLikeEntity.postLikeEntity)
			.on(QPostLikeEntity.postLikeEntity.post.id.eq(QPostEntity.postEntity.id))
			.leftJoin(QCommentEntity.commentEntity)
			.on(QCommentEntity.commentEntity.post.id.eq(QPostEntity.postEntity.id))
			.where(QPostEntity.postEntity.stock.id.eq(stockId))
			.where((sort.equalsIgnoreCase("desc") ? QPostEntity.postEntity.id.lt(offset) :
				QPostEntity.postEntity.id.gt(offset)))
			.groupBy(QPostEntity.postEntity.id, QMemberEntity.memberEntity.id, QMemberEntity.memberEntity.username,
				QPostEntity.postEntity.article, QPostEntity.postEntity.createdAt, QPostEntity.postEntity.updatedAt)
			.orderBy((sort.equalsIgnoreCase("desc") ? QPostEntity.postEntity.createdAt.desc() :
				QPostEntity.postEntity.createdAt.asc()))
			.limit(limit)
			.fetch();
	}
}
