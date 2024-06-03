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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.common.wrapper.BaseEntity;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "article", columnDefinition = "varchar(255)")
	private String article;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@Builder
	private CommentEntity(Long id, String article, PostEntity post, MemberEntity member) {
		this.id = id;
		this.article = article;
		this.post = post;
		this.member = member;
	}

	public void setPost(PostEntity post) {
		this.post = post;
		post.getComments().add(this);
	}
}
