package project.tosstock.activity.application.port.in;

public interface FollowMemberUseCase {

	Long followMember(Long followerId, Long followeeId);

	Long unfollowMember(Long followerId, Long followeeId);
}
