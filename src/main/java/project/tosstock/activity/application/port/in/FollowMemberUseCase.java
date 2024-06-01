package project.tosstock.activity.application.port.in;

import project.tosstock.activity.application.domain.model.Follow;

public interface FollowMemberUseCase {

	boolean followMember(Follow follow);
}
