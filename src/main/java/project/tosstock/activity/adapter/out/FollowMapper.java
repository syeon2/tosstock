package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.FollowEntity;
import project.tosstock.activity.application.domain.model.Follow;

@Component
public class FollowMapper {

	public FollowEntity toEntity(Follow follow) {
		return FollowEntity.builder()
			.followerId(follow.getFollowerId())
			.followeeId(follow.getFolloweeId())
			.build();
	}
}
