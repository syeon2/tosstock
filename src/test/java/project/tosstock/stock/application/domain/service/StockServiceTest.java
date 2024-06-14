package project.tosstock.stock.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.stock.adpater.out.StockPersistenceAdapter;
import project.tosstock.stock.adpater.out.persistence.StockItemRepository;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;

class StockServiceTest extends IntegrationTestSupport {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockPersistenceAdapter stockPersistenceAdapter;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockItemRepository stockItemRepository;

	@BeforeEach
	void before() {
		stockItemRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "이름을 통해 증권을 조회합니다.")
	void searchStockByName() {
		// given
		String name = "삼성";

		String name1 = name + "1";
		Stock stock1 = createStock(name1, "1234", Market.KOSPI);
		stockPersistenceAdapter.saveStock(stock1);

		String name2 = name + "2";
		Stock stock2 = createStock(name2, "1234", Market.KOSPI);
		stockPersistenceAdapter.saveStock(stock2);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> stocks = stockService.searchStockByName(name, pageable);

		// then
		assertThat(stocks).hasSize(2)
			.extracting("name")
			.contains(name1, name2);
	}

	@Test
	@DisplayName(value = "코드를 통해 증권을 조회합니다.")
	void searchStockBySymbol() {
		// given
		String symbol = "1234";
		Stock stock = createStock("hello", symbol, Market.KOSPI);
		stockPersistenceAdapter.saveStock(stock);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> stocks = stockService.searchStockBySymbol(symbol, pageable);

		// then
		assertThat(stocks).hasSize(1)
			.extracting("symbol")
			.contains(symbol);
	}

	@Test
	@DisplayName(value = "코스피/코스닥을 통해 증권을 조회합니다.")
	void searchStockByMarket() {
		// given
		String market = Market.KOSPI.getText();

		String name1 = "hello1";
		Stock stock1 = createStock(name1, "1234123", Market.KOSPI);
		stockPersistenceAdapter.saveStock(stock1);

		String name2 = "hello2";
		Stock stock2 = createStock(name2, "12341212", Market.KOSPI);
		stockPersistenceAdapter.saveStock(stock2);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> stocks = stockService.searchStockByMarket(market, pageable);

		// then
		assertThat(stocks).hasSize(2)
			.extracting("name")
			.contains(name1, name2);
	}

	private Stock createStock(String name, String symbol, Market market) {
		return Stock.builder()
			.name(name)
			.symbol(symbol)
			.originTime(LocalDateTime.now())
			.market(market)
			.build();
	}
}
