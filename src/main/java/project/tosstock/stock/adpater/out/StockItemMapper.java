package project.tosstock.stock.adpater.out;

import org.springframework.stereotype.Component;

import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.entity.StockItemEntity;
import project.tosstock.stock.application.domain.model.StockItem;

@Component
public class StockItemMapper {

	public StockItem toDomain(StockItemEntity entity) {
		return StockItem.builder()
			.id(entity.getId())
			.itemDate(entity.getItemDate())
			.openPrice(entity.getOpenPrice())
			.highPrice(entity.getHighPrice())
			.lowPrice(entity.getLowPrice())
			.closePrice(entity.getClosePrice())
			.volume(entity.getVolume())
			.build();
	}

	public StockItemEntity toEntity(StockItem stockItem, StockEntity stock) {
		return StockItemEntity.builder()
			.itemDate(stockItem.getItemDate())
			.openPrice(stockItem.getOpenPrice())
			.highPrice(stockItem.getHighPrice())
			.lowPrice(stockItem.getLowPrice())
			.closePrice(stockItem.getClosePrice())
			.volume(stockItem.getVolume())
			.stock(stock)
			.build();
	}
}
