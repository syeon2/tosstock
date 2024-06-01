package project.tosstock.activity.application.domain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.activity.application.port.out.SaveFollowPort;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowMemberUseCase {

	private final SaveFollowPort saveFollowPort;

	@Override
	public boolean followMember(Long followerId, Long followeeId) {
		saveFollowPort.save(followerId, followeeId);

		return true;
	}

}
