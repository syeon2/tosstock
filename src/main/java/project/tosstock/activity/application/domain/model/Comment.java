package project.tosstock.activity.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

	private String article;
	private Long postId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	private Comment(String article, Long postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.article = article;
		this.postId = postId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
