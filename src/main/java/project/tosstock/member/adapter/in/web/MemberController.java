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
import project.tosstock.member.adapter.in.web.request.ChangePasswordRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.SendAuthCodeByMailUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final JoinMemberUseCase joinMemberUseCase;
	private final SendAuthCodeByMailUseCase sendAuthCodeByMailUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@PostMapping("/api/v1/members")
	public ApiResult<Long> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(joinedMemberId);
	}

	@PostMapping("/api/v1/members/emails/verification-requests")
	public ApiResult<Boolean> sendAuthCodeToEmail(@Valid @RequestBody AuthEmailRequest request) {
		boolean result = sendAuthCodeByMailUseCase.sendEmail(request.getEmail());

		return ApiResult.ok(result);
	}

	@PostMapping("/api/v1/member/{id}/password")
	public ApiResult<Boolean> changePassword(
		@PathVariable("id") Long id, @RequestBody ChangePasswordRequest request) {
		boolean result = updateMemberUseCase.changePassword(id, request.getEmail(), request.getPassword());

		return ApiResult.ok(result);
	}
}
