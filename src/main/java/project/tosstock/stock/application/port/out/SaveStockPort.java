package project.tosstock.stock.application.port.out;

import project.tosstock.stock.application.domain.model.Stock;

public interface SaveStockPort {

	Long saveStock(Stock stock);
}
