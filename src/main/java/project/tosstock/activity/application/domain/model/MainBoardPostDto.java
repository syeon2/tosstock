package project.tosstock.activity.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MainBoardPostDto {

	private Long postId;
	private Member member;
	private String postArticle;
	private Integer countPostLike;
	private Integer countPostComment;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	private MainBoardPostDto(Long postId, Long memberId, String username, String postArticle, Integer countPostLike,
		Integer countPostComment, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.postId = postId;
		this.member = new Member(memberId, username);
		this.postArticle = postArticle;
		this.countPostLike = countPostLike;
		this.countPostComment = countPostComment;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Getter
	private static class Member {

		private Long id;
		private String username;

		@Builder
		private Member(Long id, String username) {
			this.id = id;
			this.username = username;
		}
	}
}
