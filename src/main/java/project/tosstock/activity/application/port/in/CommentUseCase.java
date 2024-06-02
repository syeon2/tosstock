package project.tosstock.activity.application.port.in;

import project.tosstock.activity.application.domain.model.Comment;

public interface CommentUseCase {

	Long createComment(Comment comment);

	Long removeComment(Long commentId);
}
