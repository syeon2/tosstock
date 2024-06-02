package project.tosstock.activity.application.port.in;

public interface PostLikeUseCase {

	boolean likePost(Long memberId, Long postId);

	boolean unlikePost(Long memberId, Long postId);
}
