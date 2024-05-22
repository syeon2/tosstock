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
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "", "123456789",
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
