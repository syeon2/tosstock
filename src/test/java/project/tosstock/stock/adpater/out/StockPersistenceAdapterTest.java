package project.tosstock.stock.adpater.out;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;

class StockPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private StockPersistenceAdapter stockPersistenceAdapter;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	void before() {
		stockRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "증권을 저장합니다.")
	void save() {
		// given
		Stock stock = createStock("삼송", "1111", Market.KOSPI);

		// when
		Long savedStockId = stockPersistenceAdapter.saveStock(stock);

		// then
		Optional<StockEntity> findStockOptional = stockRepository.findById(savedStockId);

		assertThat(findStockOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s.getId()).isEqualTo(savedStockId));
	}

	@Test
	@DisplayName(value = "이름으로 증권을 조회합니다. (contain)")
	void findStockByName() {
		// given
		String name = "삼성";

		String name1 = name + "1";
		String symbol1 = "112341";
		Market market1 = Market.KOSDOQ;
		Stock stock1 = createStock(name1, symbol1, market1);

		String name2 = name + "2";
		String symbol2 = "1111";
		Market market2 = Market.KOSPI;
		Stock stock2 = createStock(name2, symbol2, market2);

		stockPersistenceAdapter.saveStock(stock1);
		stockPersistenceAdapter.saveStock(stock2);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> findStocksByName = stockPersistenceAdapter.findStockByName(name, pageable);

		// then
		assertThat(findStocksByName).hasSize(2)
			.extracting("name", "symbol", "market")
			.containsExactlyInAnyOrder(
				tuple(name1, symbol1, market1),
				tuple(name2, symbol2, market2));
	}

	@Test
	@DisplayName(value = "증권 코드로 조회합니다.")
	void findStockBySymbol() {
		// given
		String name = "hello";
		String symbol = "1234";
		Market market = Market.KOSPI;
		Stock stock = createStock(name, symbol, market);

		stockPersistenceAdapter.saveStock(stock);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> findStockBySymbol = stockPersistenceAdapter.findStockBySymbol(symbol, pageable);

		// then
		assertThat(findStockBySymbol).hasSize(1)
			.extracting("name", "symbol", "market")
			.containsExactlyInAnyOrder(
				tuple(name, symbol, market));
	}

	@Test
	@DisplayName(value = "증권 코스피/코스닥으로 조회합니다.")
	void findStockByMarket() {
		// given
		Market market = Market.KOSPI;

		String name1 = "hello";
		String symbol1 = "1234";
		Stock stock1 = createStock(name1, symbol1, market);
		stockPersistenceAdapter.saveStock(stock1);

		String name2 = "bye";
		String symbol2 = "1123";
		Stock stock2 = createStock(name2, symbol2, market);
		stockPersistenceAdapter.saveStock(stock2);

		// when
		PageRequest pageable = PageRequest.of(0, 10);
		List<Stock> findStockBySymbol = stockPersistenceAdapter.findStockByMarket(market, pageable);

		// then
		assertThat(findStockBySymbol).hasSize(2)
			.extracting("name", "symbol", "market")
			.containsExactlyInAnyOrder(
				tuple(name1, symbol1, market),
				tuple(name2, symbol2, market));
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
