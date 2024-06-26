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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import project.tosstock.ControllerTestSupport;
import project.tosstock.activity.adapter.in.web.request.CreatePostRequest;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.port.in.PostingUseCase;
import project.tosstock.activity.application.port.in.SearchPostUseCase;
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;

@WebMvcTest(
	controllers = PostController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class PostControllerTest extends ControllerTestSupport {

	@MockBean
	private PostingUseCase postingUseCase;

	@MockBean
	private SearchPostUseCase searchPostUseCase;

	@Test
	@DisplayName(value = "포스트를 작성하는 API를 요청합니다.")
	void createPost() throws Exception {
		// given
		CreatePostRequest request = createPostRequest("텍스트입니다.", 1L, 1L);

		// when  // then
		mockMvc.perform(
				post("/api/v1/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isNumber())
			.andDo(document("post-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("article").type(JsonFieldType.STRING)
						.description("게시글"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER)
						.description("게시글 작성한 회원 아이디"),
					fieldWithPath("stockId").type(JsonFieldType.NUMBER)
						.description("게시글 작성할 증권 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.result").type(JsonFieldType.NUMBER)
						.description("생성된 포스트 아이디"))
			));
	}

	@Test
	@DisplayName(value = "게시글이 빈칸이면 예외를 반환합니다.")
	void create_post_exception_article_blank() throws Exception {
		// given
		CreatePostRequest request = createPostRequest("", 1L, 1L);

		// when  // then
		mockMvc.perform(
				post("/api/v1/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("게시글은 빈칸을 허용하지 않습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "회원 아이디가 null이면 예외를 반환합니다.")
	void create_post_exception_memberId_null() throws Exception {
		// given
		CreatePostRequest request = createPostRequest("텍스트 입니다.", null, 1L);

		// when  // then
		mockMvc.perform(
				post("/api/v1/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").value("회원 아이디는 필수 값입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName(value = "포스트를 삭제하는 API를 요청받습니다.")
	void remove_post() throws Exception {
		// given
		Long postId = 1L;

		// when  // then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/v1/post/{postId}", postId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.result").isNumber())
			.andDo(document("post-remove",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("삭제할 포스트 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.result").type(JsonFieldType.NUMBER)
						.description("삭제된 포스트 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "증권 종목 아이디를 기반으로 게시글을 조회합니다.")
	void searchPostByStockId() throws Exception {
		// given
		Long memberId = 1L;
		Long stockId = 1L;
		Long offset = 20L;
		Long limit = 10L;
		String sort = "desc";

		given(searchPostUseCase.searchPostByStockId(any(), any(), any()))
			.willReturn(List.of(createMainBoardPostDto("testing", stockId)));

		// when  // then
		mockMvc.perform(
				get("/api/v1/posts/stock/{stockId}", stockId)
					.param("memberId", String.valueOf(memberId))
					.param("offset", String.valueOf(offset))
					.param("limit", String.valueOf(limit))
					.param("sort", sort)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("post-search-stockId",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("stockId").description("조회하고자 하는 증권 아이디")
				),
				queryParameters(
					parameterWithName("memberId").description("자신의 회원 아이디"),
					parameterWithName("offset").description("이전 포스트 아이디"),
					parameterWithName("limit").description("포스트 조회 개수"),
					parameterWithName("sort").description("정렬")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].postId").type(JsonFieldType.NUMBER)
						.description("게시글 아이디"),
					fieldWithPath("data[].member.id").type(JsonFieldType.NUMBER)
						.description("게시글 작성 회원 아이디"),
					fieldWithPath("data[].member.username").type(JsonFieldType.STRING)
						.description("게시글 작성 회원 이름"),
					fieldWithPath("data[].postArticle").type(JsonFieldType.STRING)
						.description("게시글 내용"),
					fieldWithPath("data[].countPostLike").type(JsonFieldType.NUMBER)
						.description("게시글 좋아요 수"),
					fieldWithPath("data[].countPostComment").type(JsonFieldType.NUMBER)
						.description("게시글 댓글 수"),
					fieldWithPath("data[].isFollower").type(JsonFieldType.BOOLEAN)
						.description("게시글 작성자와의 Follow 여부"),
					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
						.description("게시글 생성일"),
					fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING)
						.description("게시글 수정일")
				))
			);
	}

	@Test
	@DisplayName(value = "텍스트를 기반으로 게시글을 조회합니다.")
	void searchPostByArticle() throws Exception {
		// given
		Long memberId = 1L;
		String article = "게시글 중 일부입니다.";
		Long offset = 20L;
		Long limit = 10L;
		String sort = "desc";

		given(searchPostUseCase.searchPostByArticle(any(), any(), any()))
			.willReturn(List.of(createMainBoardPostDto(article, 1L)));

		// when  // then
		mockMvc.perform(
				get("/api/v1/posts")
					.param("memberId", String.valueOf(memberId))
					.param("article", article)
					.param("offset", String.valueOf(offset))
					.param("limit", String.valueOf(limit))
					.param("sort", sort)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("post-search-article",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("memberId").description("자신의 회원 아이디"),
					parameterWithName("article").description("게시글 내용"),
					parameterWithName("offset").description("이전 포스트 아이디"),
					parameterWithName("limit").description("포스트 조회 개수"),
					parameterWithName("sort").description("정렬")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].postId").type(JsonFieldType.NUMBER)
						.description("게시글 아이디"),
					fieldWithPath("data[].member.id").type(JsonFieldType.NUMBER)
						.description("게시글 작성 회원 아이디"),
					fieldWithPath("data[].member.username").type(JsonFieldType.STRING)
						.description("게시글 작성 회원 이름"),
					fieldWithPath("data[].postArticle").type(JsonFieldType.STRING)
						.description("게시글 내용"),
					fieldWithPath("data[].countPostLike").type(JsonFieldType.NUMBER)
						.description("게시글 좋아요 수"),
					fieldWithPath("data[].countPostComment").type(JsonFieldType.NUMBER)
						.description("게시글 댓글 수"),
					fieldWithPath("data[].isFollower").type(JsonFieldType.BOOLEAN)
						.description("게시글 작성자와의 Follow 여부"),
					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
						.description("게시글 생성일"),
					fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING)
						.description("게시글 수정일")
				))
			);
	}

	private CreatePostRequest createPostRequest(String article, Long memberId, long stockId) {
		return CreatePostRequest.builder()
			.article(article)
			.memberId(memberId)
			.stockId(stockId)
			.build();
	}

	private MainBoardPostDto createMainBoardPostDto(String article, long id) {
		return MainBoardPostDto.builder()
			.postId(1L)
			.memberId(1L)
			.username("suyeon")
			.postArticle("hello")
			.countPostLike(0)
			.countPostComment(0)
			.isFollower(false)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}
