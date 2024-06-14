package project.tosstock.stock.application.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.tosstock.IntegrationTestSupport;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.entity.StockItemEntity;
import project.tosstock.stock.adpater.out.persistence.StockItemRepository;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.StockItem;

class StockItemServiceTest extends IntegrationTestSupport {

	@Autowired
	private StockItemService stockItemService;

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
	@DisplayName(value = "지금 시간으로부터 한달 전까지의 증권 데이터를 찾습니다.")
	void findStockFromLastMonth() {
		// given
		StockEntity savedStock = stockRepository.save(createStock());
		StockItemEntity savedStockItem
			= stockItemRepository.save(createStockItem(savedStock, LocalDateTime.now().plusDays(1).minusMonths(1)));

		// when
		List<StockItem> findStockItems = stockItemService.findStockItemsFromLastMonth(savedStock.getId());

		// then
		assertThat(findStockItems).hasSize(1);
	}

	@Test
	@DisplayName(value = "지금 시간으로부터 일년 전까지의 증권 데이터를 찾습니다.")
	void findStockFromLastYear() {
		// given
		StockEntity savedStock = stockRepository.save(createStock());
		StockItemEntity savedStockItem
			= stockItemRepository.save(createStockItem(savedStock, LocalDateTime.now().plusDays(1).minusYears(1)));

		// when
		List<StockItem> findStockItems = stockItemService.findStockItemsFromLastYear(savedStock.getId());

		// then
		assertThat(findStockItems).hasSize(1);
	}

	@Test
	@DisplayName(value = "지금 시간으로부터 3년 전까지의 증권 데이터를 찾습니다.")
	void findStockFromLastThreeYear() {
		// given
		StockEntity savedStock = stockRepository.save(createStock());
		StockItemEntity savedStockItem
			= stockItemRepository.save(createStockItem(savedStock, LocalDateTime.now().plusDays(1).minusYears(3)));

		// when
		List<StockItem> findStockItems = stockItemService.findStockItemsFromLastThreeYears(savedStock.getId());

		// then
		assertThat(findStockItems).hasSize(1);
	}

	@Test
	@DisplayName(value = "지금 시간으로부터 5년 전까지의 증권 데이터를 찾습니다.")
	void findStockFromLastFiveYears() {
		// given
		StockEntity savedStock = stockRepository.save(createStock());
		StockItemEntity savedStockItem
			= stockItemRepository.save(createStockItem(savedStock, LocalDateTime.now().plusDays(1).minusYears(5)));

		// when
		List<StockItem> findStockItems = stockItemService.findStockItemsFromLastFiveYears(savedStock.getId());

		// then
		assertThat(findStockItems).hasSize(1);
	}

	private StockItemEntity createStockItem(StockEntity stock, LocalDateTime time) {
		return StockItemEntity.builder()
			.lowPrice(1)
			.highPrice(1)
			.openPrice(1)
			.closePrice(1)
			.volume(1)
			.itemDate(time)
			.stock(stock)
			.build();
	}

	private StockEntity createStock() {
		return StockEntity.builder()
			.name("hello")
			.symbol("111)")
			.originTime(LocalDateTime.now())
			.market(Market.KOSPI)
			.build();
	}
}
