package project.tosstock.activity.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "follower_id", columnDefinition = "bigint")
	private Long followerId;

	@Column(name = "followee_id", columnDefinition = "bigint")
	private Long followeeId;

	@Builder
	private FollowEntity(Long id, Long followerId, Long followeeId) {
		this.id = id;
		this.followerId = followerId;
		this.followeeId = followeeId;
	}
}
