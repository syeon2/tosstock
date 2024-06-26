package project.tosstock.activity.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.in.web.request.CreatePostRequest;
import project.tosstock.activity.adapter.in.web.response.BasicActivityResponse;
import project.tosstock.activity.application.domain.model.CustomPage;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.port.in.PostingUseCase;
import project.tosstock.activity.application.port.in.SearchPostUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostingUseCase postingUseCase;
	private final SearchPostUseCase searchPostUseCase;

	@PostMapping("/api/v1/posts")
	public ApiResult<BasicActivityResponse<Long>> createPost(@Valid @RequestBody CreatePostRequest request) {
		Long createPostId = postingUseCase.createPost(request.toDomain());

		return ApiResult.ok(BasicActivityResponse.of(createPostId));
	}

	@DeleteMapping("/api/v1/post/{postId}")
	public ApiResult<BasicActivityResponse<Long>> removePost(@PathVariable("postId") Long postId) {
		Long removedPostId = postingUseCase.removePost(postId);

		return ApiResult.ok(BasicActivityResponse.of(removedPostId));
	}

	@GetMapping("/api/v1/posts/stock/{stockId}")
	public ApiResult<List<MainBoardPostDto>> searchPostsByStockId(
		@PathVariable("stockId") Long stockId,
		@RequestParam("memberId") Long memberId,
		@RequestParam("offset") Long offset,
		@RequestParam("limit") Long limit,
		@RequestParam("sort") String sort
	) {
		List<MainBoardPostDto> posts = searchPostUseCase.searchPostByStockId(memberId, stockId,
			CustomPage.of(offset, limit, sort));

		return ApiResult.ok(posts);
	}

	@GetMapping("/api/v1/posts")
	public ApiResult<List<MainBoardPostDto>> searchPostByArticle(
		@RequestParam("memberId") Long memberId,
		@RequestParam("article") String article,
		@RequestParam("offset") Long offset,
		@RequestParam("limit") Long limit,
		@RequestParam("sort") String sort
	) {
		List<MainBoardPostDto> posts = searchPostUseCase.searchPostByArticle(memberId, article,
			CustomPage.of(offset, limit, sort));

		return ApiResult.ok(posts);
	}
}
