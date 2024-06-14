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
import project.tosstock.member.adapter.in.web.response.BasicAuthResponse;
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
	public ApiResult<BasicAuthResponse<Long>> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(BasicAuthResponse.of(joinedMemberId));
	}

	@PostMapping("/api/v1/members/emails/verification-requests")
	public ApiResult<BasicAuthResponse<Boolean>> sendAuthCodeToEmail(@Valid @RequestBody AuthEmailRequest request) {
		boolean result = sendAuthCodeUseCase.dispatchAuthCodeToEmail(request.getEmail());

		return ApiResult.ok(BasicAuthResponse.of(result));
	}

	@PostMapping("/api/v1/member/{memberId}")
	public ApiResult<BasicAuthResponse<Boolean>> changeMemberInfo(
		@PathVariable Long memberId,
		@Valid @RequestBody ChangeMemberInfoRequest request
	) {
		boolean result = updateMemberUseCase.changeMemberInfo(memberId, request.toServiceDto());

		return ApiResult.ok(BasicAuthResponse.of(result));
	}

	@PostMapping("/api/v1/member/password")
	public ApiResult<BasicAuthResponse<Boolean>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		boolean result = updateMemberUseCase.changePassword(request.getEmail(), request.getPassword());

		return ApiResult.ok(BasicAuthResponse.of(result));
	}
}
