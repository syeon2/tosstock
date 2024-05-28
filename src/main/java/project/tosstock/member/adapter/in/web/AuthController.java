package project.tosstock.member.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.member.adapter.in.web.request.LoginRequest;
import project.tosstock.member.adapter.in.web.response.JwtTokenResponse;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.LoginUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final LoginUseCase loginUseCase;

	@GetMapping("/api/v1/login")
	public ApiResult<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
		JwtTokenDto tokenDto = loginUseCase.login(request.getEmail(), request.getPassword());

		return ApiResult.ok(JwtTokenResponse.toRequest(tokenDto));
	}
}
