package project.tosstock.activity.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

	private Long id;
	private String article;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long memberId;
	private Long stockId;

	@Builder
	private Post(Long id, String article, LocalDateTime createdAt, LocalDateTime updatedAt, Long memberId,
		Long stockId) {
		this.id = id;
		this.article = article;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberId = memberId;
		this.stockId = stockId;
	}
}
