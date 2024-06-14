package project.tosstock.newsfeed.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.newsfeed.application.domain.model.TestNewsFeed;
import project.tosstock.newsfeed.application.port.in.NewsFeedFilterUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class NewsFeedController {

	private final NewsFeedFilterUseCase newsFeedFilterUseCase;

	@GetMapping("/api/v1/newsfeed/member/{memberId}")
	public ApiResult<List<TestNewsFeed>> showBasicNewsFeed(@PathVariable("memberId") Long memberId) {
		List<TestNewsFeed> newsFeeds = newsFeedFilterUseCase.showNewsFeedBasic(memberId);

		return ApiResult.ok(newsFeeds);
	}
}
