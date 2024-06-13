package project.tosstock.stock.application.port.in;

import java.util.List;

import org.springframework.data.domain.Pageable;

import project.tosstock.stock.application.domain.model.Stock;

public interface SearchStockUseCase {

	List<Stock> searchStockByName(String name, Pageable pageable);

	List<Stock> searchStockBySymbol(String symbol, Pageable pageable);

	List<Stock> searchStockByMarket(String market, Pageable pageable);
}
