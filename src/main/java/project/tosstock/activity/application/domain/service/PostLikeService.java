package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.PostLikeUseCase;
import project.tosstock.activity.application.port.out.DeletePostLikePort;
import project.tosstock.activity.application.port.out.SavePostLikePort;

@Service
@RequiredArgsConstructor
public class PostLikeService implements PostLikeUseCase {

	private final SavePostLikePort savePostLikePort;
	private final DeletePostLikePort deletePostLikePort;

	@Override
	@Transactional
	public boolean likePost(Long memberId, Long postId) {
		savePostLikePort.save(memberId, postId);

		return true;
	}

	@Override
	@Transactional
	public boolean unlikePost(Long memberId, Long postId) {
		deletePostLikePort.delete(memberId, postId);

		return true;
	}
}
