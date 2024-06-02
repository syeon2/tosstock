package project.tosstock.activity.adapter.in.web.request;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.PostLikeUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class PostLikeController {

	private final PostLikeUseCase postLikeUseCase;

	@PostMapping("/api/v1/member/{memberId}/post/{postId}")
	public ApiResult<Boolean> likePost(
		@PathVariable("memberId") Long memberId,
		@PathVariable("postId") Long postId
	) {
		boolean result = postLikeUseCase.likePost(memberId, postId);

		return ApiResult.ok(result);
	}

	@DeleteMapping("/api/v1/member/{memberId}/post/{postId}")
	public ApiResult<Boolean> unlikePost(
		@PathVariable("memberId") Long memberId,
		@PathVariable("postId") Long postId
	) {
		boolean result = postLikeUseCase.unlikePost(memberId, postId);

		return ApiResult.ok(result);
	}
}
