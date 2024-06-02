package project.tosstock.activity.application.port.in;

import project.tosstock.activity.application.domain.model.Post;

public interface CreatePostUseCase {

	Long createPost(Post post);

	Long removePost(Long postId);
}
