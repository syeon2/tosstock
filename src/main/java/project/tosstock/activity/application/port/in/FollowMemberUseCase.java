package project.tosstock.activity.application.port.in;

public interface FollowMemberUseCase {

	boolean followMember(Long followerId, Long followeeId);

	boolean unfollowMember(Long followerId, Long followeeId);
}
