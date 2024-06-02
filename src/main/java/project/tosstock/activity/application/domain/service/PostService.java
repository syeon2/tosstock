package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.in.CreatePostUseCase;
import project.tosstock.activity.application.port.out.DeletePostPort;
import project.tosstock.activity.application.port.out.SavePostPort;

@Service
@RequiredArgsConstructor
public class PostService implements CreatePostUseCase {

	private final SavePostPort savePostPort;
	private final DeletePostPort deletePostPort;

	@Override
	@Transactional
	public Long createPost(Post post) {
		return savePostPort.save(post);
	}

	@Override
	public Long removePost(Long postId) {
		return deletePostPort.delete(postId);
	}
}
