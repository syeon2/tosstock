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
import project.tosstock.stock.application.domain.model.StockItem;
import project.tosstock.stock.application.port.in.FindStockItemUseCase;

@WebMvcTest(
	controllers = StockItemController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtVerificationFilter.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
	}
)
class StockItemControllerTest extends ControllerTestSupport {

	@MockBean
	private FindStockItemUseCase findStockItemUseCase;

	@Test
	@DisplayName(value = "현 시간에서 한달 전까지 전 증권 차트 데이터를 조회합니다.")
	void findStockItemsFromLastMonth() throws Exception {
		// given
		List<StockItem> stockItems = List.of(createStockItem());

		given(findStockItemUseCase.findStockItemsFromLastMonth(any()))
			.willReturn(stockItems);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/{stockId}/details/month", 1)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stockItem-findMonth",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("stockId").description("증권 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 차트 데이터 아이디"),
					fieldWithPath("data[].itemDate").type(JsonFieldType.STRING)
						.description("증권 차트 데이터 일자"),
					fieldWithPath("data[].openPrice").type(JsonFieldType.NUMBER)
						.description("증권 개장가"),
					fieldWithPath("data[].closePrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].highPrice").type(JsonFieldType.NUMBER)
						.description("증권 고가"),
					fieldWithPath("data[].lowPrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].volume").type(JsonFieldType.NUMBER)
						.description("증권 거래량"),
					fieldWithPath("data[].stockId").type(JsonFieldType.NUMBER)
						.description("증권 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "현 시간에서 일년 전까지 전 증권 차트 데이터를 조회합니다.")
	void findStockItemsFromLastYear() throws Exception {
		// given
		List<StockItem> stockItems = List.of(createStockItem());

		given(findStockItemUseCase.findStockItemsFromLastYear(any()))
			.willReturn(stockItems);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/{stockId}/details/year", 1)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stockItem-findYear",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("stockId").description("증권 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 차트 데이터 아이디"),
					fieldWithPath("data[].itemDate").type(JsonFieldType.STRING)
						.description("증권 차트 데이터 일자"),
					fieldWithPath("data[].openPrice").type(JsonFieldType.NUMBER)
						.description("증권 개장가"),
					fieldWithPath("data[].closePrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].highPrice").type(JsonFieldType.NUMBER)
						.description("증권 고가"),
					fieldWithPath("data[].lowPrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].volume").type(JsonFieldType.NUMBER)
						.description("증권 거래량"),
					fieldWithPath("data[].stockId").type(JsonFieldType.NUMBER)
						.description("증권 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "현 시간에서 3년 전까지 전 증권 차트 데이터를 조회합니다.")
	void findStockItemsFromLastThreeYears() throws Exception {
		// given
		List<StockItem> stockItems = List.of(createStockItem());

		given(findStockItemUseCase.findStockItemsFromLastThreeYears(any()))
			.willReturn(stockItems);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/{stockId}/details/three-years", 1)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stockItem-findThreeYears",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("stockId").description("증권 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 차트 데이터 아이디"),
					fieldWithPath("data[].itemDate").type(JsonFieldType.STRING)
						.description("증권 차트 데이터 일자"),
					fieldWithPath("data[].openPrice").type(JsonFieldType.NUMBER)
						.description("증권 개장가"),
					fieldWithPath("data[].closePrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].highPrice").type(JsonFieldType.NUMBER)
						.description("증권 고가"),
					fieldWithPath("data[].lowPrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].volume").type(JsonFieldType.NUMBER)
						.description("증권 거래량"),
					fieldWithPath("data[].stockId").type(JsonFieldType.NUMBER)
						.description("증권 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "현 시간에서 5년 전까지 전 증권 차트 데이터를 조회합니다.")
	void findStockItemsFromLastFiveYears() throws Exception {
		// given
		List<StockItem> stockItems = List.of(createStockItem());

		given(findStockItemUseCase.findStockItemsFromLastFiveYears(any()))
			.willReturn(stockItems);

		// when  // then
		mockMvc.perform(
				get("/api/v1/stocks/{stockId}/details/five-years", 1)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").isNumber())
			.andExpect(jsonPath("$.message").isEmpty())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(document("stockItem-findFiveYears",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("stockId").description("증권 아이디")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER)
						.description("상태 코드"),
					fieldWithPath("message").type(JsonFieldType.NULL)
						.description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY)
						.description("응답 데이터 DTO"),
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("증권 차트 데이터 아이디"),
					fieldWithPath("data[].itemDate").type(JsonFieldType.STRING)
						.description("증권 차트 데이터 일자"),
					fieldWithPath("data[].openPrice").type(JsonFieldType.NUMBER)
						.description("증권 개장가"),
					fieldWithPath("data[].closePrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].highPrice").type(JsonFieldType.NUMBER)
						.description("증권 고가"),
					fieldWithPath("data[].lowPrice").type(JsonFieldType.NUMBER)
						.description("증권 종가"),
					fieldWithPath("data[].volume").type(JsonFieldType.NUMBER)
						.description("증권 거래량"),
					fieldWithPath("data[].stockId").type(JsonFieldType.NUMBER)
						.description("증권 아이디")
				)
			));
	}

	private StockItem createStockItem() {
		return StockItem.builder()
			.id(1L)
			.itemDate(LocalDateTime.now())
			.openPrice(1)
			.closePrice(1)
			.highPrice(1)
			.lowPrice(1)
			.volume(1)
			.stockId(1L)
			.build();
	}
}
