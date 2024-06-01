package project.tosstock.activity.application.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Follow {

	private Long followerId;
	private Long followeeId;

	@Builder
	private Follow(Long followerId, Long followeeId) {
		this.followerId = followerId;
		this.followeeId = followeeId;
	}
}
