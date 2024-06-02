package project.tosstock.activity.application.port.out;

import project.tosstock.activity.application.domain.model.Comment;

public interface SaveCommentPort {

	Long save(Comment comment);
}
