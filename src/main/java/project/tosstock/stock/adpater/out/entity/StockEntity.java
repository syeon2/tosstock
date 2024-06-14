package project.tosstock.stock.adpater.out.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.stock.application.domain.model.Market;

@Getter
@Entity
@Table(name = "stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "symbol", columnDefinition = "varchar(60)")
	private String symbol;

	@Column(name = "name", columnDefinition = "varchar(255)")
	private String name;

	@Column(name = "origin_time", columnDefinition = "timestamp")
	private LocalDateTime originTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "market", columnDefinition = "varchar(60)")
	private Market market;

	@OneToMany(
		mappedBy = "stock",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private List<StockItemEntity> stockItems = new ArrayList<>();

	@Builder
	private StockEntity(Long id, String symbol, String name, LocalDateTime originTime, Market market) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		this.originTime = originTime;
		this.market = market;
	}

	public void addStockItem(StockItemEntity stockItem) {
		this.stockItems.add(stockItem);
		stockItem.setStock(this);
	}
}
