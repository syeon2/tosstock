package project.tosstock.activity.application.port.out;

public interface SavePostLikePort {

	Long save(Long memberId, Long postId);
}
