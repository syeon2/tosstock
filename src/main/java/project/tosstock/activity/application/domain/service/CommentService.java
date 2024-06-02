package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.activity.application.port.in.CommentUseCase;
import project.tosstock.activity.application.port.out.DeleteCommentPort;
import project.tosstock.activity.application.port.out.SaveCommentPort;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final SaveCommentPort saveCommentPort;
	private final DeleteCommentPort deleteCommentPort;

	@Override
	@Transactional
	public Long createComment(Comment comment) {
		return saveCommentPort.save(comment);
	}

	@Override
	@Transactional
	public Long removeComment(Long commentId) {
		return deleteCommentPort.delete(commentId);
	}
}
