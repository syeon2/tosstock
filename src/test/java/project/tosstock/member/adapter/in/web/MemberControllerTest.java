package project.tosstock.member.adapter.in.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;
import project.tosstock.member.adapter.in.web.request.ChangeMemberInfoRequest;
import project.tosstock.member.adapter.in.web.request.ChangePasswordRequest;
import project.tosstock.member.adapter.in.web.request.JoinMemberRequest;
import project.tosstock.member.adapter.in.web.request.VerificationEmailRequest;
import project.tosstock.member.application.port.in.JoinMemberUseCase;
import project.tosstock.member.application.port.in.SendVerificationEmailCodeUseCase;
import project.tosstock.member.application.port.in.UpdateMemberUseCase;

@WebMvcTest(
	controllers = MemberController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class MemberControllerTest extends ControllerTestSupport {

	@MockBean
	private JoinMemberUseCase joinMemberUseCase;

	@MockBean
	private SendVerificationEmailCodeUseCase sendVerificationEmailCodeUseCase;

	@MockBean
	private UpdateMemberUseCase updateMemberUseCase;

	@Test
	@DisplayName(value = "유저가 회원가입에 성공합니다.")
	void joinMember() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "123456789",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isNumber())
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
						.description("프로필 이미지"),
					fieldWithPath("authCode").type(JsonFieldType.STRING)
						.description("인증 번호")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("데이터"),
					fieldWithPath("data.result").type(JsonFieldType.NUMBER)
						.description("회원 고유 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (이름 필드 null)")
	void joinMember_exception_nullUsername() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", null, "123456789",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_wrongEmailPattern() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94", "suyeon", "123456789",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joiMember_exception_nullEmail() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest(null, "suyeon", "123456789",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_nullPassword() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", null,
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_lessLengthPassword() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "1234567",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_overLengthPassword() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "012345678901234567891",
			"01022223333", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_insertedStringInPhoneNumber() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"0101111222a", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_lessLengthPhoneNumber() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"010", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_overLengthPhoneNumber() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781",
			"010111122221", "000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
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
	void joinMember_exception_nullPhoneNumber() throws Exception {
		// given
		JoinMemberRequest request = createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781", null,
			"000000");

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("전화번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "유저가 회원가입에 실패합니다. (인증코드 null)")
	void joinMember_exception_nullAuthCode() throws Exception {
		// given
		JoinMemberRequest request =
			createJoinMemberRequest("waterkite94@gmail.com", "suyeon", "213456781", "00011112222", null);

		// when  // then
		mockMvc.perform(
				post("/api/v1/members")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("이메일 인증 번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "이메일을 통해 회원가입을 위한 인증 코드를 보냅니다.")
	void sendAuthCodeToEmail() throws Exception {
		// given
		VerificationEmailRequest request = new VerificationEmailRequest("waterkite94@gmail.com");

		// when // then
		mockMvc.perform(
				post("/api/v1/members/emails/verification-requests")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isBoolean())
			.andDo(document("member-email-auth",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("인증 받을 이메일")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("데이터"),
					fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
						.description("요청 성공 여부")
				)
			));
	}

	@Test
	@DisplayName(value = "인증코드 요청시 이메일은 필수 값입니다.")
	void sendAuthCodeToEmail_exception_nullEmail() throws Exception {
		// given
		VerificationEmailRequest request = new VerificationEmailRequest(null);

		// when // then
		mockMvc.perform(
				post("/api/v1/members/emails/verification-requests")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value("400"))
			.andExpect(jsonPath("$.message").value("인증 요청을 위한 이메일 값은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 업데이트합니다.")
	void changePassword() throws Exception {
		ChangePasswordRequest request = createChangePasswordRequest("waterkite94@gmail.com", "12345678");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isBoolean())
			.andDo(document("member-update-password",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("변경할 비밀번호의 이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("변경할 비밀번호")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("데이터"),
					fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
						.description("요청 성공 여부")
				)));
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경 시 비밀번호는 필수 값입니다.")
	void changePassword_exception_nullPassword() throws Exception {
		ChangePasswordRequest request = createChangePasswordRequest("waterkite94@gmail.com", null);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("비밀번호는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경 시 비밀번호는 최소 8자 이상입니다.")
	void changePassword_exception_lessLengthPassword() throws Exception {
		ChangePasswordRequest request = createChangePasswordRequest("waterkite94@gmail.com", "1234567");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경 시 비밀번호는 최대 20자 이상입니다.")
	void changePassword_exception_overLengthPassword() throws Exception {
		ChangePasswordRequest request = createChangePasswordRequest("waterkite94@gmail.com", "123456789012345678901");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("비밀번호는 8 ~ 20자리입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 비밀번호를 변경 시 이메일은 필수 값입니다.")
	void changePassword_exception_nullEmail() throws Exception {
		ChangePasswordRequest request = createChangePasswordRequest(null, "12345678");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("이메일은 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 정보를 변경합니다.")
	void changeMemberInfo() throws Exception {
		// given
		Long memberId = 1L;
		ChangeMemberInfoRequest request = ChangeMemberInfoRequest.builder()
			.username("suyeon")
			.introduce("안녕하세요.")
			.profileImageUrl("www.github.com/syeon2")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/{memberId}", memberId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isBoolean())
			.andDo(document("member-update-info",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 아이디")
				),
				requestFields(
					fieldWithPath("username").type(JsonFieldType.STRING)
						.description("회원 아이디"),
					fieldWithPath("introduce").type(JsonFieldType.STRING)
						.description("자기소개"),
					fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
						.description("프로필 이미지 URL")
						.optional()
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("데이터"),
					fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
						.description("요청 성공 여부")
				)
			));
	}

	@Test
	@DisplayName(value = "회원 정보를 변경 시 이름은 빈칸을 허용하지 않습니다.")
	void changeMemberInfo_notBlankUsername() throws Exception {
		// given
		Long memberId = 1L;
		ChangeMemberInfoRequest request = ChangeMemberInfoRequest.builder()
			.username("")
			.introduce("안녕하세요.")
			.profileImageUrl("www.github.com/syeon2")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/{memberId}", memberId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("이름은 빈칸을 허용하지 않습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 정보를 변경 시 자기소개는 null을 허용하지 않습니다.")
	void changeMemberInfo_notBlank() throws Exception {
		// given
		Long memberId = 1L;
		ChangeMemberInfoRequest request = ChangeMemberInfoRequest.builder()
			.username("suyeon")
			.introduce(null)
			.profileImageUrl("www.github.com/syeon2")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/{memberId}", memberId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("자기소개는 null을 허용하지 않습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	private static ChangePasswordRequest createChangePasswordRequest(String email, String password) {
		return ChangePasswordRequest.builder()
			.email(email)
			.password(password)
			.build();
	}

	private JoinMemberRequest createJoinMemberRequest(String email, String username, String password,
		String phoneNumber, String authCode) {
		return JoinMemberRequest.builder()
			.email(email)
			.username(username)
			.password(password)
			.phoneNumber(phoneNumber)
			.introduce("hello")
			.profileImageUrl("https://syeon2.github.io/")
			.authCode(authCode)
			.build();
	}
}
