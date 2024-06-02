package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.application.domain.model.Comment;

@Component
public class CommentMapper {

	public CommentEntity toEntity(Comment comment, PostEntity post) {
		return CommentEntity.builder()
			.article(comment.getArticle())
			.post(post)
			.build();
	}
}
