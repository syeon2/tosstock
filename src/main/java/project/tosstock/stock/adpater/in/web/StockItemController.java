package project.tosstock.stock.adpater.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.stock.application.domain.model.StockItem;
import project.tosstock.stock.application.port.in.FindStockItemUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class StockItemController {

	private final FindStockItemUseCase findStockItemUseCase;

	@GetMapping("/api/v1/stocks/{stockId}/details/month")
	private ApiResult<List<StockItem>> findStockItemsFromLastMonth(@PathVariable("stockId") Long stockId) {
		List<StockItem> findStockItems = findStockItemUseCase.findStockItemsFromLastMonth(stockId);

		return ApiResult.ok(findStockItems);
	}

	@GetMapping("/api/v1/stocks/{stockId}/details/year")
	private ApiResult<List<StockItem>> findStockItemsFromLastYear(@PathVariable("stockId") Long stockId) {
		List<StockItem> findStockItems = findStockItemUseCase.findStockItemsFromLastYear(stockId);

		return ApiResult.ok(findStockItems);
	}

	@GetMapping("/api/v1/stocks/{stockId}/details/three-years")
	private ApiResult<List<StockItem>> findStockItemsFromLastThreeYears(@PathVariable("stockId") Long stockId) {
		List<StockItem> findStockItems = findStockItemUseCase.findStockItemsFromLastThreeYears(stockId);

		return ApiResult.ok(findStockItems);
	}

	@GetMapping("/api/v1/stocks/{stockId}/details/five-years")
	private ApiResult<List<StockItem>> findStockItemsFromLastFiveYears(@PathVariable("stockId") Long stockId) {
		List<StockItem> findStockItems = findStockItemUseCase.findStockItemsFromLastFiveYears(stockId);

		return ApiResult.ok(findStockItems);
	}
}
