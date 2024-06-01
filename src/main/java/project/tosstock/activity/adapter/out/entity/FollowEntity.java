package project.tosstock.activity.adapter.out.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "follow",
	uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
@IdClass(FollowEntity.PK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowEntity {

	@Id
	@Column(name = "follower_id", columnDefinition = "bigint")
	private Long followerId;

	@Id
	@Column(name = "followee_id", columnDefinition = "bigint")
	private Long followeeId;

	@Builder
	private FollowEntity(Long followerId, Long followeeId) {
		this.followerId = followerId;
		this.followeeId = followeeId;
	}

	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class PK implements Serializable {
		private Long followerId;
		private Long followeeId;

		@Builder
		private PK(Long followerId, Long followeeId) {
			this.followerId = followerId;
			this.followeeId = followeeId;
		}
	}
}
