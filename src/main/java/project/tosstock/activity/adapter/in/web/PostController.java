package project.tosstock.activity.adapter.in.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.in.web.request.CreatePostRequest;
import project.tosstock.activity.application.port.in.CreatePostUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class PostController {

	private final CreatePostUseCase createPostUseCase;

	@PostMapping("/api/v1/posts")
	public ApiResult<Long> createPost(@Valid @RequestBody CreatePostRequest request) {
		Long createPostId = createPostUseCase.createPost(request.toDomain());

		return ApiResult.ok(createPostId);
	}

	@DeleteMapping("/api/v1/post/{postId}")
	public ApiResult<Long> removePost(@PathVariable("postId") Long postId) {
		Long removedPostId = createPostUseCase.removePost(postId);

		return ApiResult.ok(removedPostId);
	}
}
