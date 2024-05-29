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
import project.tosstock.common.config.web.filter.JwtAuthenticationFilter;
import project.tosstock.common.config.web.filter.JwtFilter;
import project.tosstock.member.adapter.in.web.request.LoginRequest;
import project.tosstock.member.application.domain.model.JwtTokenDto;
import project.tosstock.member.application.port.in.LoginUseCase;

@WebMvcTest(
	controllers = AuthController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
	}
)
class AuthControllerTest extends ControllerTestSupport {

	@MockBean
	private LoginUseCase loginUseCase;

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

		given(loginUseCase.login(email, password, address))
			.willReturn(JwtTokenDto.builder()
				.accessToken("accessToken")
				.refreshToken("refreshToken")
				.build()
			);

		// when  // then
		mockMvc.perform(
				get("/api/v1/login")
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
				get("/api/v1/login")
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
				get("/api/v1/login")
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
				get("/api/v1/login")
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
				get("/api/v1/login")
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
				get("/api/v1/login")
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
				get("/api/v1/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("기기 주소는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}
}
