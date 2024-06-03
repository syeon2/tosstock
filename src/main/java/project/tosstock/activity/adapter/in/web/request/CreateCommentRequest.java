package project.tosstock.activity.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.activity.application.domain.model.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentRequest {

	@NotBlank(message = "댓글은 빈칸을 허용하지 않습니다.")
	private String article;

	@NotNull(message = "포스트 아이디는 필수 값입니다.")
	private Long postId;

	@NotNull(message = "회원 아이디는 필수 값입니다.")
	private Long memberId;

	@Builder
	private CreateCommentRequest(String article, Long postId, Long memberId) {
		this.article = article;
		this.postId = postId;
		this.memberId = memberId;
	}

	public Comment toDomain() {
		return Comment.builder()
			.article(this.article)
			.memberId(this.memberId)
			.postId(this.postId)
			.build();
	}
}
