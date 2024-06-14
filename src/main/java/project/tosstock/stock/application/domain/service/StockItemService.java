package project.tosstock.stock.application.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.stock.application.domain.model.StockItem;
import project.tosstock.stock.application.port.in.FindStockItemUseCase;
import project.tosstock.stock.application.port.out.FindStockItemPort;

@Service
@RequiredArgsConstructor
public class StockItemService implements FindStockItemUseCase {

	private final FindStockItemPort findStockItemPort;

	@Transactional(readOnly = true)
	@Override
	public List<StockItem> findStockItemsFromLastMonth(Long stockId) {
		LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);

		return findStockItemPort.findStockItemsByStockIdAndTimeAfter(stockId, lastMonth);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StockItem> findStockItemsFromLastYear(Long stockId) {
		LocalDateTime lastMonth = LocalDateTime.now().minusYears(1);

		return findStockItemPort.findStockItemsByStockIdAndTimeAfter(stockId, lastMonth);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StockItem> findStockItemsAfterThreeYears(Long stockId) {
		LocalDateTime lastMonth = LocalDateTime.now().minusYears(3);

		return findStockItemPort.findStockItemsByStockIdAndTimeAfter(stockId, lastMonth);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StockItem> findStockItemsAfterFiveYears(Long stockId) {
		LocalDateTime lastMonth = LocalDateTime.now().minusYears(5);

		return findStockItemPort.findStockItemsByStockIdAndTimeAfter(stockId, lastMonth);
	}
}
