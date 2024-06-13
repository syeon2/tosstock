package project.tosstock.stock.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StockItem {

	private Long id;
	private LocalDateTime itemDate;
	private int openPrice;
	private int highPrice;
	private int lowPrice;
	private int closePrice;
	private int volume;
	private Long stockId;

	@Builder
	private StockItem(Long id, LocalDateTime itemDate, int openPrice, int highPrice, int lowPrice, int closePrice,
		int volume, Long stockId) {
		this.id = id;
		this.itemDate = itemDate;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.stockId = stockId;
	}
}
