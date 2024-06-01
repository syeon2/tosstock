package project.tosstock.activity.application.port.out;

public interface SaveFollowPort {

	void save(Long followerId, Long followeeId);
}
