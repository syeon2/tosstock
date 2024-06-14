package project.tosstock.stock.adpater.out.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "stock_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_item_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "item_date", columnDefinition = "timestamp")
	private LocalDateTime itemDate;

	@Column(name = "open_price", columnDefinition = "int")
	private int openPrice;

	@Column(name = "high_price", columnDefinition = "int")
	private int highPrice;

	@Column(name = "low_price", columnDefinition = "int")
	private int lowPrice;

	@Column(name = "close_price", columnDefinition = "int")
	private int closePrice;

	@Column(name = "volume", columnDefinition = "int")
	private int volume;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id")
	private StockEntity stock;

	@Builder
	private StockItemEntity(Long id, LocalDateTime itemDate, int openPrice, int highPrice, int lowPrice, int closePrice,
		int volume, StockEntity stock) {
		this.id = id;
		this.itemDate = itemDate;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.stock = stock;
	}
}
