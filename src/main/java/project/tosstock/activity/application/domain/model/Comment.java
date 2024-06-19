package project.tosstock.activity.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

	private Long id;
	private String article;
	private Long postId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long memberId;

	@Builder
	private Comment(Long id, String article, Long postId, LocalDateTime createdAt, LocalDateTime updatedAt,
		Long memberId) {
		this.id = id;
		this.article = article;
		this.postId = postId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberId = memberId;
	}
}
