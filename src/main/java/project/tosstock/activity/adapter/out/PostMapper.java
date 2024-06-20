package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.stock.adpater.out.entity.StockEntity;

@Component
public class PostMapper {

	public PostEntity toEntity(MemberEntity member, StockEntity stock, Post post) {
		return PostEntity.builder()
			.article(post.getArticle())
			.member(member)
			.stock(stock)
			.build();
	}

	public Post toDomain(PostEntity post) {
		return Post.builder()
			.id(post.getId())
			.article(post.getArticle())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.memberId(post.getMember().getId())
			.stockId(post.getStock().getId())
			.build();
	}

	public MainBoardPostDto toMainPostDto(
		PostEntity entity,
		int countPostLike,
		int countComment
	) {
		return MainBoardPostDto.builder()
			.postId(entity.getId())
			.memberId(entity.getMember().getId())
			.username(entity.getMember().getUsername())
			.postArticle(entity.getArticle())
			.countPostLike(countPostLike)
			.countPostComment(countComment)
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
