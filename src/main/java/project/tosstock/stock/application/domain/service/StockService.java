package project.tosstock.stock.application.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;
import project.tosstock.stock.application.port.in.SearchStockUseCase;
import project.tosstock.stock.application.port.out.FindStockPort;

@Service
@RequiredArgsConstructor
public class StockService implements SearchStockUseCase {

	private final FindStockPort findStockPort;

	@Transactional(readOnly = true)
	@Override
	public List<Stock> searchStockByName(String name, Pageable pageable) {
		return findStockPort.findStockByName(name, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Stock> searchStockBySymbol(String symbol, Pageable pageable) {
		return findStockPort.findStockBySymbol(symbol, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Stock> searchStockByMarket(String market, Pageable pageable) {
		return findStockPort.findStockByMarket(Market.convertStringToEnum(market), pageable);
	}
}
