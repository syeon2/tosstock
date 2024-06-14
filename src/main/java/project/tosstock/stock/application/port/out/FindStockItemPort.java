package project.tosstock.stock.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

import project.tosstock.stock.application.domain.model.StockItem;

public interface FindStockItemPort {

	List<StockItem> findStockItemsByStockIdAndTimeAfter(Long stockId, LocalDateTime time);
}
