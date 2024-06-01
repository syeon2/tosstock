package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.FollowEntity;

@Component
public class FollowMapper {

	public FollowEntity toEntity(Long followerId, Long followeeId) {
		return FollowEntity.builder()
			.followerId(followerId)
			.followeeId(followeeId)
			.build();
	}
}
