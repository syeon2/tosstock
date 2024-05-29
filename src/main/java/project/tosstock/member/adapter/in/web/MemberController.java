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
import project.tosstock.member.adapter.in.web.request.ChangeProfileImageUrlRequest;
import project.tosstock.member.adapter.in.web.request.ChangeUsernameRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.application.port.in.EmailForMemberUseCase;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final JoinMemberUseCase joinMemberUseCase;
	private final EmailForMemberUseCase emailForMemberUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@PostMapping("/api/v1/members")
	public ApiResult<Long> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(joinedMemberId);
	}

	@PostMapping("/api/v1/emails/verification-requests")
	public ApiResult<Boolean> sendAuthCodeToEmail(@Valid @RequestBody AuthEmailRequest request) {
		boolean result = emailForMemberUseCase.sendEmail(request.getEmail());

		return ApiResult.ok(result);
	}

	@PostMapping("/api/v1/member/{id}/username")
	public ApiResult<Boolean> changeUsername(
		@PathVariable("id") Long id, @RequestBody ChangeUsernameRequest request
	) {
		updateMemberUseCase.updateUsername(id, request.getUsername());

		return ApiResult.ok(true);
	}

	@PostMapping("/api/v1/member/{id}/profile_image_url")
	public ApiResult<Boolean> changeProfileImageUrl(
		@PathVariable("id") Long id, @RequestBody ChangeProfileImageUrlRequest request) {
		updateMemberUseCase.updateProfileImageUrl(id, request.getProfileImageUrl());

		return ApiResult.ok(true);
	}

	@PostMapping("/api/v1/member/{id}/password")
	public ApiResult<Boolean> changePassword(
		@PathVariable("id") Long id, @RequestBody ChangePasswordRequest request) {
		updateMemberUseCase.updatePassword(id, request.getEmail(), request.getPassword());

		return ApiResult.ok(true);
	}
}
