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

	@Builder
	private CreateCommentRequest(String article, Long postId) {
		this.article = article;
		this.postId = postId;
	}

	public Comment toDomain() {
		return Comment.builder()
			.article(this.article)
			.postId(this.postId)
			.build();
	}
}
