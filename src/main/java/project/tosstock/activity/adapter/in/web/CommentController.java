package project.tosstock.activity.adapter.in.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.in.web.request.CreateCommentRequest;
import project.tosstock.activity.adapter.in.web.response.BasicActivityResponse;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.activity.application.port.in.CommentUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentUseCase commentUseCase;

	@PostMapping("/api/v1/posts/comments")
	private ApiResult<BasicActivityResponse<Long>> createComment(@Valid @RequestBody CreateCommentRequest request) {
		Long createdCommentId = commentUseCase.createComment(request.toDomain());

		return ApiResult.ok(BasicActivityResponse.of(createdCommentId));
	}

	@DeleteMapping("/api/v1/posts/comment/{commentId}")
	private ApiResult<BasicActivityResponse<Long>> removeComment(@PathVariable("commentId") Long commentId) {
		Long removedCommentId = commentUseCase.removeComment(commentId);

		return ApiResult.ok(BasicActivityResponse.of(removedCommentId));
	}

	@GetMapping("/api/v1/comments/post/{postId}")
	public ApiResult<List<Comment>> fetchPostComments(@PathVariable("postId") Long postId, Pageable pageable) {
		List<Comment> findComments = commentUseCase.fetchPostComments(postId, pageable);

		return ApiResult.ok(findComments);
	}
}
