package project.tosstock.stock.application.port.out;

import java.util.List;

import org.springframework.data.domain.Pageable;

import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;

public interface FindStockPort {

	List<Stock> findStockByName(String name, Pageable pageable);

	List<Stock> findStockBySymbol(String symbol, Pageable pageable);

	List<Stock> findStockByMarket(Market market, Pageable pageable);
}
