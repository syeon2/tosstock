package project.tosstock.activity.application.port.out;

import project.tosstock.activity.application.domain.model.Follow;

public interface SaveFollowPort {

	void save(Follow follow);
}
