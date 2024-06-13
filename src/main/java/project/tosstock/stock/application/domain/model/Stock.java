package project.tosstock.stock.application.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Stock {

	private Long id;
	private String symbol;
	private String name;
	private LocalDateTime originTime;
	private Market market;

	@Builder
	private Stock(Long id, String symbol, String name, LocalDateTime originTime, Market market) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		this.originTime = originTime;
		this.market = market;
	}
}
