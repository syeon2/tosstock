package project.tosstock.stock.adpater.out;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.Market;
import project.tosstock.stock.application.domain.model.Stock;
import project.tosstock.stock.application.port.out.FindStockPort;
import project.tosstock.stock.application.port.out.SaveStockPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class StockPersistenceAdapter implements SaveStockPort, FindStockPort {

	private final StockRepository stockRepository;
	private final StockMapper stockMapper;

	@Override
	public Long saveStock(Stock stock) {
		StockEntity savedStock = stockRepository.save(stockMapper.toEntity(stock));

		return savedStock.getId();
	}

	@Override
	public List<Stock> findStockByName(String name, Pageable pageable) {
		Page<StockEntity> findStockPageable = stockRepository.findByNameContaining(name, pageable);

		return findStockPageable.getContent().stream()
			.map(stockMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<Stock> findStockBySymbol(String symbol, Pageable pageable) {
		Page<StockEntity> findStockPageable = stockRepository.findBySymbol(symbol, pageable);

		return findStockPageable.getContent().stream()
			.map(stockMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<Stock> findStockByMarket(Market market, Pageable pageable) {
		Page<StockEntity> findStockPageable = stockRepository.findByMarket(market, pageable);

		return findStockPageable.getContent().stream()
			.map(stockMapper::toDomain)
			.collect(Collectors.toList());
	}
}
