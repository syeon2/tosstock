package project.tosstock.newfeed.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class NewsFeedController {

	private final NewsFeedFilterUseCase newsFeedFilterUseCase;

	@GetMapping("/api/v1/newsfeed/member/{memberId}")
	public ApiResult<List<NewsFeed>> showBasicNewsFeed(@PathVariable("memberId") Long memberId) {
		List<NewsFeed> newsFeeds = newsFeedFilterUseCase.showNewsFeedBasic(memberId);

		return ApiResult.ok(newsFeeds);
	}
}
