package project.tosstock.activity.adapter.in.web;

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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.activity.application.port.in.FollowMemberUseCase;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;

@WebMvcTest(
	controllers = FollowController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class FollowControllerTest extends ControllerTestSupport {

	@MockBean
	private FollowMemberUseCase followMemberUseCase;

	@Test
	@DisplayName(value = "follower id인 팔로워가 다른 회원(followee id)을 팔로우합니다.")
	void follow_member() throws Exception {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/follower/{followerId}/followee/{followeeId}", followerId, followeeId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isNumber())
			.andDo(document("member-follow",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("followerId").description("팔로우 주체 아이디"),
					parameterWithName("followeeId").description("팔로우 된 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("팔로우 고유 아이디")
				)));
	}

	@Test
	@DisplayName(value = "follower id인 팔로워가 다른 회원(followee id)을 언팔로우합니다.")
	void unfollow_member() throws Exception {
		// given
		Long followerId = 1L;
		Long followeeId = 2L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/member/follower/{followerId}/followee/{followeeId}", followerId, followeeId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isNumber())
			.andDo(document("member-unfollow",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("followerId").description("팔로우 주체 아이디"),
					parameterWithName("followeeId").description("팔로우 된 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("언팔로우 고유 아이디")
				)));
	}
}
