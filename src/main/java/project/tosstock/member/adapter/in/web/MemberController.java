package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.AuthEmailRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.application.port.in.EmailForMemberUseCase;
import project.tosstock.member.application.port.in.JoinMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final JoinMemberUseCase joinMemberUseCase;
	private final EmailForMemberUseCase emailForMemberUseCase;

	@PostMapping("/api/v1/member")
	public ApiResult<Long> joinMember(@Valid @RequestBody JoinMemberRequest request) {
		Long joinedMemberId = joinMemberUseCase.joinMember(request.toDomain(), request.getAuthCode());

		return ApiResult.ok(joinedMemberId);
	}

	@PostMapping("/api/v1/member/emails/verification-requests")
	public ApiResult<Boolean> sendAuthCodeToEmail(@Valid @RequestBody AuthEmailRequest request) {
		boolean result = emailForMemberUseCase.sendEmail(request.getEmail());

		return ApiResult.ok(result);
	}
}
