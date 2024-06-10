package project.tosstock.activity.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicPostResponse {

	private Long result;

	public BasicPostResponse(Long result) {
		this.result = result;
	}

	public static BasicPostResponse of(Long postId) {
		return new BasicPostResponse(postId);
	}
}
