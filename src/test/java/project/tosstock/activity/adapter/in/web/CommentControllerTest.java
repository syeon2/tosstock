package project.tosstock.activity.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.activity.adapter.in.web.request.CreateCommentRequest;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.activity.application.port.in.CommentUseCase;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;

@WebMvcTest(
	controllers = CommentController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class CommentControllerTest extends ControllerTestSupport {

	@MockBean
	private CommentUseCase commentUseCase;

	@Test
	@DisplayName(value = "댓글을 생성하는 API를 호출합니다.")
	void create_comment() throws Exception {
		// given
		CreateCommentRequest request = createCommentRequest();

		// when  // then
		mockMvc.perform(
				post("/api/v1/posts/comments")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").exists())
			.andDo(document("comment-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("article").type(JsonFieldType.STRING)
						.description("댓글 내용"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER)
						.description("회원 아이디"),
					fieldWithPath("postId").type(JsonFieldType.NUMBER)
						.description("댓글 단 포스트 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.result").type(JsonFieldType.NUMBER)
						.description("저장된 댓글 아이디")
				)));
	}

	@Test
	@DisplayName(value = "댓글을 삭제하는 API를 호출합니다.")
	void remove_comment() throws Exception {
		// given
		Long commentId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/posts/comment/{commentId}", commentId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isNumber())
			.andDo(document("comment-remove",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("commentId").description("삭제할 댓글 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.result").type(JsonFieldType.NUMBER)
						.description("삭제된 댓글 아이디")
				)));
	}

	@Test
	@DisplayName(value = "게시판 아이디를 통해 게시판에 대한 댓글을 조회합니다.")
	void fetchPostComments() throws Exception {
		// given
		Long postId = 1L;

		given(commentUseCase.fetchPostComments(any(), any()))
			.willReturn(List.of(createComment()));

		// when  // then
		mockMvc.perform(
				get("/api/v1/comments/post/{postId}", postId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("comment-fetch-postId",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("게시글 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].article").type(JsonFieldType.STRING)
						.description("댓글"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("댓글 아이디"),
					fieldWithPath("data[].postId").type(JsonFieldType.NUMBER)
						.description("게시글 아이디"),
					fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER)
						.description("회원 아이디"),
					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
						.description("댓글 생성일"),
					fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING)
						.description("댓글 수정일")
				))
			);
	}

	private CreateCommentRequest createCommentRequest() {
		return CreateCommentRequest.builder()
			.article("댓글")
			.postId(1L)
			.memberId(1L)
			.build();
	}

	private Comment createComment() {
		return Comment.builder()
			.id(1L)
			.article("댓글")
			.memberId(1L)
			.postId(1L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}
