package project.tosstock.stock.adpater.out;

import org.springframework.stereotype.Component;

import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.application.domain.model.Stock;

@Component
public class StockMapper {

	public Stock toDomain(StockEntity entity) {
		return Stock.builder()
			.id(entity.getId())
			.name(entity.getName())
			.symbol(entity.getSymbol())
			.originTime(entity.getOriginTime())
			.market(entity.getMarket())
			.build();
	}

	public StockEntity toEntity(Stock stock) {
		return StockEntity.builder()
			.name(stock.getName())
			.symbol(stock.getSymbol())
			.originTime(stock.getOriginTime())
			.market(stock.getMarket())
			.build();
	}
}
