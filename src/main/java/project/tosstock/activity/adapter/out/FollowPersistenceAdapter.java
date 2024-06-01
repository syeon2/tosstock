package project.tosstock.activity.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.application.domain.model.Follow;
import project.tosstock.activity.application.port.out.SaveFollowPort;
import project.tosstock.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements SaveFollowPort {

	private final FollowRepository followRepository;
	private final FollowMapper followMapper;

	@Override
	public void save(Follow follow) {
		followRepository.save(followMapper.toEntity(follow));
	}
}
