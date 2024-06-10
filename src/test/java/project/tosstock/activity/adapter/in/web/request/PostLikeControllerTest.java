package project.tosstock.activity.adapter.in.web.request;

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
import project.tosstock.activity.adapter.in.web.PostLikeController;
import project.tosstock.activity.application.port.in.PostLikeUseCase;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;

@WebMvcTest(
	controllers = PostLikeController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class PostLikeControllerTest extends ControllerTestSupport {

	@MockBean
	private PostLikeUseCase postLikeUseCase;

	@Test
	@DisplayName(value = "회원이 게시글을 좋아요 상태로 변경하는 API를 호출합니다.")
	void like_post() throws Exception {
		// given
		Long memberId = 1L;
		Long postId = 1L;

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/{memberId}/post/{postId}/like", memberId, postId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isBoolean())
			.andDo(document("like-post-like",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 아이디"),
					parameterWithName("postId").description("게시글 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터 DTO"),
					fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
						.description("좋아요 요청 성공 여부")
				)));
	}

	@Test
	@DisplayName(value = "회원이 게시글을 좋아요 상태를 해제하는 API를 호출합니다.")
	void unlike_post() throws Exception {
		// given
		Long memberId = 1L;
		Long postId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/member/{memberId}/post/{postId}/like", memberId, postId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isBoolean())
			.andDo(document("like-post-unlike",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 아이디"),
					parameterWithName("postId").description("게시글 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터 DTO"),
					fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
						.description("좋아요 해제 요청 성공 여부")
				)));
	}
}
