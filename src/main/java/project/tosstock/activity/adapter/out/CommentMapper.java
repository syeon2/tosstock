package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Component
public class CommentMapper {

	public CommentEntity toEntity(Comment comment, MemberEntity member, PostEntity post) {
		return CommentEntity.builder()
			.article(comment.getArticle())
			.post(post)
			.member(member)
			.build();
	}

	public Comment toDomain(CommentEntity entity) {
		return Comment.builder()
			.id(entity.getId())
			.article(entity.getArticle())
			.postId(entity.getPost().getId())
			.memberId(entity.getMember().getId())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
