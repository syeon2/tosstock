package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.activity.application.port.out.DeleteFollowPort;
import project.tosstock.activity.application.port.out.SaveFollowPort;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowMemberUseCase {

	private final SaveFollowPort saveFollowPort;
	private final DeleteFollowPort deleteFollowPort;

	@Override
	public boolean followMember(Long followerId, Long followeeId) {
		saveFollowPort.save(followerId, followeeId);

		return true;
	}

	@Override
	public boolean unfollowMember(Long followerId, Long followeeId) {
		deleteFollowPort.delete(followerId, followeeId);

		return true;
	}

}
