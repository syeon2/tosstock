package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.ChangeMemberInfoRequest;
import project.tosstock.member.adapter.in.web.request.ChangePasswordRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.adapter.in.web.request.VerificationEmailRequest;
import project.tosstock.member.adapter.in.web.response.BasicAuthenticationResponse;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.SendVerificationEmailCodeUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final JoinMemberUseCase joinMemberUseCase;
	private final SendVerificationEmailCodeUseCase sendVerificationEmailCodeUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@PostMapping("/api/v1/members")
	public ApiResult<BasicAuthenticationResponse<Long>> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(BasicAuthenticationResponse.of(joinedMemberId));
	}

	@PostMapping("/api/v1/members/emails/verification-requests")
	public ApiResult<BasicAuthenticationResponse<Boolean>> sendAuthCodeToEmail(
		@Valid @RequestBody VerificationEmailRequest request) {
		boolean result = sendVerificationEmailCodeUseCase.sendAuthCodeToEmail(request.getEmail());

		return ApiResult.ok(BasicAuthenticationResponse.of(result));
	}

	@PostMapping("/api/v1/member/{memberId}")
	public ApiResult<BasicAuthenticationResponse<Boolean>> changeMemberInfo(
		@PathVariable Long memberId,
		@Valid @RequestBody ChangeMemberInfoRequest request
	) {
		boolean result = updateMemberUseCase.changeMemberInfo(memberId, request.toServiceDto());

		return ApiResult.ok(BasicAuthenticationResponse.of(result));
	}

	@PostMapping("/api/v1/member/password")
	public ApiResult<BasicAuthenticationResponse<Boolean>> changePassword(
		@Valid @RequestBody ChangePasswordRequest request) {
		boolean result = updateMemberUseCase.changePassword(request.getEmail(), request.getPassword());

		return ApiResult.ok(BasicAuthenticationResponse.of(result));
	}
}
