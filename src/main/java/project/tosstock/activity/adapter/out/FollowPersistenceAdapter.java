package project.tosstock.activity.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.application.port.out.DeleteFollowPort;
import project.tosstock.activity.application.port.out.SaveFollowPort;
import project.tosstock.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements SaveFollowPort, DeleteFollowPort {

	private final FollowRepository followRepository;
	private final FollowMapper followMapper;

	@Override
	public void save(Long followerId, Long followeeId) {
		followRepository.save(followMapper.toEntity(followerId, followeeId));
	}

	@Override
	public void delete(Long followerId, Long followeeId) {
		followRepository.deleteById(followMapper.createFollowCompositeKey(followerId, followeeId));
	}
}
