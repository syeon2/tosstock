package project.tosstock.activity.adapter.out.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tosstock.common.wrapper.BaseEntity;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", columnDefinition = "bigint")
	private Long id;

	@Lob
	@Column(name = "article", columnDefinition = "text")
	private String article;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@Builder
	private PostEntity(Long id, String article, MemberEntity member) {
		this.id = id;
		this.article = article;
		this.member = member;
	}
}
