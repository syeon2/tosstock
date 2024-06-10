package project.tosstock.newfeed.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import project.tosstock.newfeed.application.domain.model.FeedType;
import project.tosstock.newfeed.application.domain.model.TestNewsFeed;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;

@WebMvcTest(
	controllers = NewsFeedController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class NewsFeedControllerTest extends ControllerTestSupport {

	@MockBean
	private NewsFeedFilterUseCase newsFeedFilterUseCase;

	@Test
	void showBasicNewsFeed() throws Exception {
		// given
		Long memberId = 1L;

		TestNewsFeed hello = TestNewsFeed.builder()
			.id(1L)
			.feedId(1L)
			.feedType(FeedType.POST)
			.username("hello")
			.article("안녕하세요>")
			.build();

		given(newsFeedFilterUseCase.showNewsFeedBasic(any()))
			.willReturn(List.of(hello));

		// when  // then
		mockMvc.perform(
				get("/api/v1/newsfeed/member/{memberId}", memberId)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("newsfeed-show",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("memberId").description("회원 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("뉴스피드 아이디"),
					fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER)
						.description("피드 아이디"),
					fieldWithPath("data[].feedType").type(JsonFieldType.STRING)
						.description("피트 타입"),
					fieldWithPath("data[].username").type(JsonFieldType.STRING)
						.description("피드 작성 회원 이름"),
					fieldWithPath("data[].article").type(JsonFieldType.STRING)
						.description("피드 내용")
				)));
	}
}
