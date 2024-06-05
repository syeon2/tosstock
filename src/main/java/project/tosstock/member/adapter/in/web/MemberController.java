package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.AuthEmailRequest;
import project.tosstock.member.adapter.in.web.request.ChangeMemberInfoRequest;
import project.tosstock.member.adapter.in.web.request.ChangePasswordRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.adapter.in.web.response.BasicResponse;
import project.tosstock.member.adapter.in.web.response.JoinMemberResponse;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.SendAuthCodeUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final JoinMemberUseCase joinMemberUseCase;
	private final SendAuthCodeUseCase sendAuthCodeUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@PostMapping("/api/v1/members")
	public ApiResult<JoinMemberResponse> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(JoinMemberResponse.of(joinedMemberId));
	}

	@PostMapping("/api/v1/members/emails/verification-requests")
	public ApiResult<BasicResponse> sendAuthCodeToEmail(@Valid @RequestBody AuthEmailRequest request) {
		boolean result = sendAuthCodeUseCase.dispatchAuthCodeToEmail(request.getEmail());

		return ApiResult.ok(BasicResponse.of(result));
	}

	@PostMapping("/api/v1/member/{memberId}")
	public ApiResult<BasicResponse> changeMemberInfo(
		@PathVariable("memberId") Long memberId,
		@Valid @RequestBody ChangeMemberInfoRequest request
	) {
		boolean result = updateMemberUseCase.changeMemberInfo(memberId, request.toServiceDto());

		return ApiResult.ok(BasicResponse.of(result));
	}

	@PostMapping("/api/v1/member/password")
	public ApiResult<BasicResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		boolean result = updateMemberUseCase.changePassword(request.getEmail(), request.getPassword());

		return ApiResult.ok(BasicResponse.of(result));
	}
}
