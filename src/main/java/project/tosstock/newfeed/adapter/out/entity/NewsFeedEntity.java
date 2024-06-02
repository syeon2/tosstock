package project.tosstock.newfeed.adapter.out.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.common.wrapper.BaseEntity;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.newfeed.application.domain.model.FeedType;

@Getter
@Entity
@Table(name = "newfeed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsFeedEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "newfeed_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "feed_id", columnDefinition = "bigint")
	private Long feedId;

	@Enumerated(EnumType.STRING)
	@Column(name = "feedType", columnDefinition = "varchar(60)")
	private FeedType feedType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@Builder
	private NewsFeedEntity(Long id, Long feedId, FeedType feedType, MemberEntity member) {
		this.id = id;
		this.feedId = feedId;
		this.feedType = feedType;
		this.member = member;
	}
}
