package project.tosstock.stock.application.port.in;

import java.util.List;

import project.tosstock.stock.application.domain.model.StockItem;

public interface FindStockItemUseCase {

	List<StockItem> findStockItemsFromLastMonth(Long stockId);

	List<StockItem> findStockItemsFromLastYear(Long stockId);

	List<StockItem> findStockItemsAfterThreeYears(Long stockId);

	List<StockItem> findStockItemsAfterFiveYears(Long stockId);
}
