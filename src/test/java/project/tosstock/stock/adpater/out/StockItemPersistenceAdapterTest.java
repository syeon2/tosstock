package project.tosstock.stock.adpater.out;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

class StockItemPersistenceAdapterTest extends IntegrationTestSupport {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockItemRepository stockItemRepository;

	@Autowired
	private StockItemPersistenceAdapter stockItemPersistenceAdapter;

	@BeforeEach
	void before() {
		stockItemRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName(value = "증권 세부 차트 데이터를 저장합니다.")
	void save() {
		// given
		StockEntity savedStock = stockRepository.save(createStock());
		StockItem stockItem = createStockItem(savedStock.getId(), LocalDateTime.now());

		// when
		Long savedStockItem = stockItemPersistenceAdapter.save(stockItem);

		// then
		Optional<StockItemEntity> findStockItemOptional = stockItemRepository.findById(savedStockItem);

		assertThat(findStockItemOptional).isPresent()
			.hasValueSatisfying(s -> assertThat(s.getId()).isEqualTo(savedStockItem));
	}

	@Test
	@DisplayName(value = "특정 증권 아이디와 특정 날짜 이후의 증권 세부 차트 데이터를 조회합니다.")
	void findByStockItemAndTimestampAfter() {
		// given
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime prevMonth = now.minusMonths(1);

		StockEntity savedStock = stockRepository.save(createStock());
		stockItemPersistenceAdapter.save(createStockItem(savedStock.getId(), now));

		// when
		List<StockItem> findStockItemsByStockIdAndTimeAfter
			= stockItemPersistenceAdapter.findStockItemsByStockIdAndTimeAfter(savedStock.getId(), prevMonth);

		// then
		assertThat(findStockItemsByStockIdAndTimeAfter).hasSize(1);
	}

	private StockItem createStockItem(Long stockId, LocalDateTime time) {
		return StockItem.builder()
			.lowPrice(1)
			.highPrice(1)
			.openPrice(1)
			.closePrice(1)
			.volume(1)
			.itemDate(time)
			.stockId(stockId)
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
