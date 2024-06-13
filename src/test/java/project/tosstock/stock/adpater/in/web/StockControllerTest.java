package project.tosstock.stock.adpater.in.web;

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
import project.tosstock.common.config.web.WebConfig;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;
import project.tosstock.stock.application.port.in.SearchStockUseCase;

@WebMvcTest(
	controllers = StockController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class StockControllerTest extends ControllerTestSupport {

	@MockBean
	private SearchStockUseCase searchStockUseCase;

	@Test
	@DisplayName(value = "입력한 문자가 포함된 이름을 가진 증권 리스트를 검색합니다.")
	void searchStockByName() throws Exception {
		// given
		String name = "company";
		List<Stock> stocks = List.of(createStock());

		given(searchStockUseCase.searchStockByName(any(), any()))
			.willReturn(stocks);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/n")
					.contentType(MediaType.APPLICATION_JSON)
					.param("name", name)
					.param("page", "0"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stock-search-name",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("name").description("증권 이름"),
					parameterWithName("page").description("페이지 순서")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 아이디"),
					fieldWithPath("data[].symbol").type(JsonFieldType.STRING)
						.description("증권 코드"),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description("증권 이름"),
					fieldWithPath("data[].originTime").type(JsonFieldType.STRING)
						.description("증권 시작일자"),
					fieldWithPath("data[].market").type(JsonFieldType.STRING)
						.description("증권 시장 (코스피 or 코스닥)")
				)
			));
	}

	@Test
	@DisplayName(value = "입력한 문자가 포함된 코드를 가진 증권 리스트를 검색합니다.")
	void searchStockBySymbol() throws Exception {
		// given
		String symbol = "11111";
		List<Stock> stocks = List.of(createStock());

		given(searchStockUseCase.searchStockBySymbol(any(), any()))
			.willReturn(stocks);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/s")
					.contentType(MediaType.APPLICATION_JSON)
					.param("symbol", symbol)
					.param("page", "0"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stock-search-symbol",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("symbol").description("증권 코드"),
					parameterWithName("page").description("페이지 순서")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 아이디"),
					fieldWithPath("data[].symbol").type(JsonFieldType.STRING)
						.description("증권 코드"),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description("증권 이름"),
					fieldWithPath("data[].originTime").type(JsonFieldType.STRING)
						.description("증권 시작일자"),
					fieldWithPath("data[].market").type(JsonFieldType.STRING)
						.description("증권 시장 (코스피 or 코스닥)")
				)
			));
	}

	@Test
	@DisplayName(value = "코스닥 or 코스피인 증권 리스트를 검색합니다.")
	void searchStockByMarket() throws Exception {
		// given
		String market = "kospi";
		List<Stock> stocks = List.of(createStock());

		given(searchStockUseCase.searchStockByMarket(any(), any()))
			.willReturn(stocks);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/m")
					.contentType(MediaType.APPLICATION_JSON)
					.param("market", market)
					.param("page", "0"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stock-search-market",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("market").description("증권 시장"),
					parameterWithName("page").description("페이지 순서")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 아이디"),
					fieldWithPath("data[].symbol").type(JsonFieldType.STRING)
						.description("증권 코드"),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description("증권 이름"),
					fieldWithPath("data[].originTime").type(JsonFieldType.STRING)
						.description("증권 시작일자"),
					fieldWithPath("data[].market").type(JsonFieldType.STRING)
						.description("증권 시장 (코스피 or 코스닥)")
				)
			));
	}

	private static Stock createStock() {
		return Stock.builder()
			.id(1L)
			.symbol("1111")
			.name("hello")
			.originTime(LocalDateTime.now())
			.market(Market.KOSPI)
			.build();
	}
}
