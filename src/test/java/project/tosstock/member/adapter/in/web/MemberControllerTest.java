package project.tosstock.member.adapter.in.web;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.application.port.in.JoinMemberUseCase;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTestSupport {

	@MockBean
	private JoinMemberUseCase joinMemberUseCase;

	@Test
	@DisplayName(value = "유저가 회원가입에 성공합니다.")
	void join_member() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "123456789",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
			.andDo(document("member-join",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("username").type(JsonFieldType.STRING)
						.description("회원 이름"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("introduce").type(JsonFieldType.STRING)
						.optional()
						.description("자기 소개"),
					fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
						.optional()
						.description("프로필 이미지")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("가입된 회원 고유번호")
				)
			));
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (이름 필드 null)")
	void join_member_exception_by_username() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", null, "123456789",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("이름은 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (이메일 필드 패턴 X)")
	void join_member_exception_by_email_Pattern() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94", "suyeon", "123456789",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("이메일 형식으로 가입 가능합니다. (ex. xxx@xxx.com)"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (이메일 필드 null)")
	void join_member_exception_by_email_null() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest(null, "suyeon", "123456789",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("이메일은 필수값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (비밀번호 null)")
	void join_member_exception_by_password_null() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", null,
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("비밀번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (비밀번호 less min length)")
	void join_member_exception_by_password_less_length() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "1234567",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (비밀번호 over max length)")
	void join_member_exception_by_password_over_length() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "012345678901234567891",
			"01022223333");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (전화번호 패턴 문자 삽입)")
	void join_member_exception_by_phone_number_pattern_insert_text() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"0101111222a");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (전화번호 패턴 less length)")
	void join_member_exception_by_phone_number_pattern_less_length() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"010");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (전화번호 패턴 over length)")
	void join_member_exception_by_phone_number_pattern_over_length() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"010111122221");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (전화번호 null)")
	void join_member_exception_by_phone_number_null() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781", null);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("전화번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	private JoinMemberRequest createJoinMemberRequest(String email, String username, String password,
		String phoneNumber) {
		return JoinMemberRequest.builder()
			.email(email)
			.username(username)
			.password(password)
			.phoneNumber(phoneNumber)
			.introduce("hello")
			.profileImageUrl("https://syeon2.github.io/")
			.build();
	}
}
