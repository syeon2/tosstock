package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.LoginRequest;
import project.tosstock.member.adapter.in.web.request.LogoutAllRequest;
import project.tosstock.member.adapter.in.web.request.LogoutRequest;
import project.tosstock.member.adapter.in.web.request.RefreshTokenRequest;
import project.tosstock.member.adapter.in.web.response.JwtTokenResponse;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthMemberUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthMemberUseCase authMemberUseCase;

	@GetMapping("/api/v1/auth/login")
	public ApiResult<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
		JwtTokenDto tokenDto = authMemberUseCase.login(request.getEmail(), request.getPassword(), request.getAddress());

		return ApiResult.ok(JwtTokenResponse.toRequest(tokenDto));
	}

	@DeleteMapping("/api/v1/auth/logout")
	public ApiResult<Boolean> logout(@Valid @RequestBody LogoutRequest request) {
		authMemberUseCase.logout(request.getEmail(), request.getAddress());

		return ApiResult.ok(true);
	}

	@DeleteMapping("/api/v1/auth/logout-all")
	public ApiResult<Boolean> logoutAll(@Valid @RequestBody LogoutAllRequest request) {
		authMemberUseCase.logoutAll(request.getEmail());

		return ApiResult.ok(true);
	}

	@PostMapping("/api/v1/auth/refresh-token")
	public ApiResult<JwtTokenResponse> updateJwtToken(@Valid @RequestBody RefreshTokenRequest request) {
		JwtTokenDto jwtTokenDto = authMemberUseCase.updateJwtToken(request.getRefreshToken());

		return ApiResult.ok(JwtTokenResponse.toRequest(jwtTokenDto));
	}
}
