package project.tosstock.activity.application.port.in;

import java.util.List;

import org.springframework.data.domain.Pageable;

import project.tosstock.activity.application.domain.model.Comment;

public interface CommentUseCase {

	Long createComment(Comment comment);

	Long removeComment(Long commentId);

	List<Comment> fetchPostComments(Long postId, Pageable pageable);
}
