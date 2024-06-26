package project.tosstock.activity.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MainBoardPostDto {

	private Long postId;
	private MemberDto member;
	private String postArticle;
	private Integer countPostLike;
	private Integer countPostComment;
	private Boolean isFollower;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder
	private MainBoardPostDto(Long postId, Long memberId, String username, String postArticle, Integer countPostLike,
		Integer countPostComment, Boolean isFollower, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.postId = postId;
		this.member = new MemberDto(memberId, username);
		this.postArticle = postArticle;
		this.countPostLike = countPostLike;
		this.countPostComment = countPostComment;
		this.isFollower = isFollower;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public MainBoardPostDto(Long postId, MemberDto member, String postArticle, Integer countPostLike,
		Integer countPostComment, Boolean isFollower, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.postId = postId;
		this.member = member;
		this.postArticle = postArticle;
		this.countPostLike = countPostLike;
		this.countPostComment = countPostComment;
		this.isFollower = isFollower;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
