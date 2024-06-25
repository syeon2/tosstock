package project.tosstock.activity.application.port.out;

import java.util.List;

import org.springframework.data.domain.Pageable;

import project.tosstock.activity.application.domain.model.Comment;

public interface FindCommentPort {

	List<Comment> findCommentByPostId(Long postId, Pageable pageable);
}
