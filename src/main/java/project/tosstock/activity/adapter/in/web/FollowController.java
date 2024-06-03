package project.tosstock.activity.adapter.in.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class FollowController {

	private final FollowMemberUseCase followMemberUseCase;

	@PostMapping("/api/v1/member/follower/{followerId}/followee/{followeeId}")
	private ApiResult<Long> followMember(
		@PathVariable("followerId") Long followerId,
		@PathVariable("followeeId") Long followeeId
	) {
		return ApiResult.ok(followMemberUseCase.followMember(followerId, followeeId));
	}

	@DeleteMapping("/api/v1/member/follower/{followerId}/followee/{followeeId}")
	private ApiResult<Long> unfollowMember(
		@PathVariable("followerId") Long followerId,
		@PathVariable("followeeId") Long followeeId
	) {
		return ApiResult.ok(followMemberUseCase.unfollowMember(followerId, followeeId));
	}
}
