package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.LoginRequest;
import project.tosstock.member.adapter.in.web.response.JwtTokenResponse;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthUseCase;
import project.tosstock.member.application.port.in.LoginUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final LoginUseCase loginUseCase;
	private final AuthUseCase authUseCase;

	@GetMapping("/api/v1/login")
	public ApiResult<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
		JwtTokenDto tokenDto = loginUseCase.login(request.getEmail(), request.getPassword(), request.getAddress());

		return ApiResult.ok(JwtTokenResponse.toRequest(tokenDto));
	}

	@GetMapping("/api/v1/auth")
	public ApiResult<JwtTokenResponse> authenticateRefreshToken(
		@RequestParam("email") String email,
		@RequestParam("refresh-token") String refreshToken
	) {
		JwtTokenDto jwtTokenDto = authUseCase.renewTokens(email, refreshToken);

		return ApiResult.ok(JwtTokenResponse.toRequest(jwtTokenDto));
	}
}
