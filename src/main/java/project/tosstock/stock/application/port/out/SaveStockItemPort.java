package project.tosstock.stock.application.port.out;

import project.tosstock.stock.application.domain.model.StockItem;

public interface SaveStockItemPort {

	Long save(StockItem stockItem);
}
