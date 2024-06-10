package project.tosstock.activity.application.port.out;

import java.util.Optional;

import project.tosstock.activity.application.domain.model.Post;

public interface FindPostPort {

	Optional<Post> findPostById(Long postId);
}
