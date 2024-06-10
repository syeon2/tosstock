package project.tosstock.activity.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.common.wrapper.BaseEntity;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Getter
@Entity
@Table(
	name = "post_like",
	uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "post_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_like_id", columnDefinition = "bigint")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@Builder
	private PostLikeEntity(Long id, MemberEntity member, PostEntity post) {
		this.id = id;
		this.member = member;
		this.post = post;
	}
}
