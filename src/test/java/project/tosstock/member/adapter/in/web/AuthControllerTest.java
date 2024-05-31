package project.tosstock.member.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;
import project.tosstock.member.adapter.in.web.request.LoginRequest;
import project.tosstock.member.adapter.in.web.request.LogoutAllRequest;
import project.tosstock.member.adapter.in.web.request.LogoutRequest;
import project.tosstock.member.adapter.in.web.request.RefreshTokenRequest;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.AuthMemberUseCase;

@WebMvcTest(
	controllers = AuthController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class AuthControllerTest extends ControllerTestSupport {

	@MockBean
	private AuthMemberUseCase authMemberUseCase;

	@Test
	@DisplayName(value = "이메일과 비밀번호, 기기 주소를 받아 로그인에 성공합니다.")
	void login() throws Exception {
		// given
		String email = "waterkite94@gmail.com";
		String password = "12345678";
		String address = "1";

		LoginRequest request = LoginRequest.builder()
			.email(email)
			.password(password)
			.address(address)
			.build();

		given(authMemberUseCase.login(email, password, address))
			.willReturn(JwtTokenDto.builder()
				.accessToken("accessToken")
				.refreshToken("refreshToken")
				.build()
			);

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isNotEmpty())
			.andExpect(jsonPath("$.data.accessToken").isString())
			.andExpect(jsonPath("$.data.refreshToken").isString())
			.andDo(document("member-login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("기기 주소")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터 DTO"),
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
						.description("Access Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
						.description("Refresh Token")
				)
			));
	}

	@Test
	@DisplayName(value = "이메일 형식이 아닌 텍스트로 로그인할 시 예외를 반환합니다.")
	void login_exception_non_pattern() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("waterkite")
			.password("12345678")
			.address("1")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("아이디는 이메일 형식입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "빈 문자열로 이메일을 입력 후 로그인할 시 예외를 반환합니다.")
	void login_exception_blank_email() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("")
			.password("12345678")
			.address("1")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("이메일은 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "빈 문자열로 비밀번호를 입력 후 로그인할 시 예외를 반환합니다.")
	void login_exception_blank_password() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("waterkite04@gmail.com")
			.address("1")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("비밀번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "비밀번호는 최소 8자리로 입력해야합니다.")
	void login_exception_min_password() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("waterkite04@gmail.com")
			.password("1234567")
			.address("1")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20 자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "비밀번호는 최대 20자리로 입력해야합니다.")
	void login_exception_max_password() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("waterkite04@gmail.com")
			.password("123456789012345678901")
			.address("1")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20 자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "기기 주소는 필수 값입니다.")
	void login_exception_null_address() throws Exception {
		// given
		LoginRequest request = LoginRequest.builder()
			.email("waterkite04@gmail.com")
			.password("12345678")
			.build();

		// when  // then
		mockMvc.perform(
				get("/api/v1/auth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("기기 주소는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "Access Token이 만료되어 Refresh Token을 받아 새로운 jwt token을 반환합니다.")
	void authenticateRefreshToken() throws Exception {
		// given
		String refreshToken = "1234";
		RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

		given(authMemberUseCase.updateJwtToken(refreshToken))
			.willReturn(JwtTokenDto.builder()
				.accessToken("access token")
				.refreshToken("refresh token")
				.build());

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isNotEmpty())
			.andExpect(jsonPath("$.data.accessToken").isString())
			.andExpect(jsonPath("$.data.refreshToken").isString())
			.andDo(
				document("member-auth-refresh-token",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestFields(
						fieldWithPath("refreshToken").type(JsonFieldType.STRING)
							.description("Refresh Token")
					),
					responseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER)
							.description("상태 코드"),
						fieldWithPath("message").type(JsonFieldType.NULL)
							.description("메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT)
							.description("응답 데이터 DTO"),
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
							.description("Access token"),
						fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
							.description("Refresh token")
					)
				));
	}

	@Test
	@DisplayName(value = "Access Token이 만료되어 새로운 토큰을 요청할 때 Refresh Token은 필수 값입니다.")
	void authenticateRefreshToken_null() throws Exception {
		// given
		String refreshToken = "";
		RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("Refresh Token은 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "이메일과 기기 주소를 받아 로그아웃합니다.")
	void logout() throws Exception {
		// given
		LogoutRequest request = LogoutRequest.builder()
			.address("1")
			.email("waterkite94@gmail.com")
			.build();

		// when  // then
		mockMvc.perform(
				delete("/api/v1/auth/logout")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isBoolean())
			.andDo(document("member-logout",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("기기 주소")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.BOOLEAN)
						.description("로그아웃 성공 값")
				)));
	}

	@Test
	@DisplayName(value = "이메일을 통해 모든 기기에서 로그아웃합니다.")
	void logoutAll() throws Exception {
		// given
		LogoutAllRequest request = new LogoutAllRequest("waterkite94@gmail.com");

		// when  // then
		mockMvc.perform(
				delete("/api/v1/auth/logout-all")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isBoolean())
			.andDo(document("member-logout-all",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.BOOLEAN)
						.description("로그아웃 성공 값")
				)));
	}
}
