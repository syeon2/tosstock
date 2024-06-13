package project.tosstock.stock.adpater.in.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.WebAdapter;
import project.tosstock.common.wrapper.ApiResult;
import project.tosstock.stock.application.domain.model.Stock;
import project.tosstock.stock.application.port.in.SearchStockUseCase;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class StockController {

	private final SearchStockUseCase searchStockUseCase;

	@GetMapping("/api/v1/stocks/n")
	public ApiResult<List<Stock>> searchStockByName(@RequestParam String name, Pageable pageable) {
		List<Stock> stocks = searchStockUseCase.searchStockByName(name, pageable);

		return ApiResult.ok(stocks);
	}

	@GetMapping("/api/v1/stocks/s")
	public ApiResult<List<Stock>> searchStockBySymbol(@RequestParam String symbol, Pageable pageable) {
		List<Stock> stocks = searchStockUseCase.searchStockBySymbol(symbol, pageable);

		return ApiResult.ok(stocks);
	}

	@GetMapping("/api/v1/stocks/m")
	public ApiResult<List<Stock>> searchStockByMarket(@RequestParam String market, Pageable pageable) {
		List<Stock> stocks = searchStockUseCase.searchStockByMarket(market, pageable);

		return ApiResult.ok(stocks);
	}
}
