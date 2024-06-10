package project.tosstock.activity.adapter.in.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.in.web.request.CreatePostRequest;
import project.tosstock.activity.adapter.in.web.response.BasicActivityResponse;
import project.tosstock.activity.application.port.in.PostingUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostingUseCase postingUseCase;

	@PostMapping("/api/v1/posts")
	public ApiResult<BasicActivityResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
		Long createPostId = postingUseCase.createPost(request.toDomain());

		return ApiResult.ok(BasicActivityResponse.of(createPostId));
	}

	@DeleteMapping("/api/v1/post/{postId}")
	public ApiResult<BasicActivityResponse> removePost(@PathVariable("postId") Long postId) {
		Long removedPostId = postingUseCase.removePost(postId);

		return ApiResult.ok(BasicActivityResponse.of(removedPostId));
	}
}
