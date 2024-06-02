package project.tosstock.activity.application.port.out;

public interface DeletePostLikePort {

	boolean delete(Long memberId, Long postId);
}
