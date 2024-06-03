package project.tosstock.activity.application.port.out;

public interface SaveFollowPort {

	Long save(Long followerId, Long followeeId);
}
