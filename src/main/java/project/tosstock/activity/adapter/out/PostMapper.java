package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Component
public class PostMapper {

	public PostEntity toEntity(MemberEntity member, Post post) {
		return PostEntity.builder()
			.article(post.getArticle())
			.member(member)
			.stockId(post.getStockId())
			.build();
	}

	public Post toDomain(PostEntity post) {
		return Post.builder()
			.id(post.getId())
			.article(post.getArticle())
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.memberId(post.getMember().getId())
			.stockId(post.getStockId())
			.build();
	}
}
