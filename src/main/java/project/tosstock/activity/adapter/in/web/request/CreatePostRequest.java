package project.tosstock.activity.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.activity.application.domain.model.Post;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

	@NotBlank(message = "게시글은 빈칸을 허용하지 않습니다.")
	private String article;

	@NotNull(message = "회원 아이디는 필수 값입니다.")
	private Long memberId;

	@NotNull(message = "증권 아이디는 필수 값입니다.")
	private Long stockId;

	@Builder
	private CreatePostRequest(String article, Long memberId, Long stockId) {
		this.article = article;
		this.memberId = memberId;
		this.stockId = stockId;
	}

	public Post toDomain() {
		return Post.builder()
			.article(this.article)
			.memberId(this.memberId)
			.stockId(this.stockId)
			.build();
	}
}
