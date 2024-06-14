package project.tosstock.stock.adpater.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.entity.StockItemEntity;
import project.tosstock.stock.adpater.out.persistence.StockItemRepository;
import project.tosstock.stock.adpater.out.persistence.StockRepository;
import project.tosstock.stock.application.domain.model.StockItem;
import project.tosstock.stock.application.port.out.FindStockItemPort;
import project.tosstock.stock.application.port.out.SaveStockItemPort;

@PersistenceAdapter
@RequiredArgsConstructor
public class StockItemPersistenceAdapter implements SaveStockItemPort, FindStockItemPort {

	private final StockRepository stockRepository;
	private final StockItemRepository stockItemRepository;
	private final StockItemMapper stockItemMapper;

	@Override
	public Long save(StockItem stockItem) {
		StockEntity proxyStock = stockRepository.getReferenceById(stockItem.getStockId());
		StockItemEntity savedStockItem = stockItemRepository.save(stockItemMapper.toEntity(stockItem, proxyStock));

		return savedStockItem.getId();
	}

	@Override
	public List<StockItem> findStockItemsByStockIdAndTimeAfter(Long stockId, LocalDateTime time) {
		return stockItemRepository.findByStockIdAndItemDateAfter(stockId, time).stream()
			.map(stockItemMapper::toDomain)
			.collect(Collectors.toList());
	}
}
