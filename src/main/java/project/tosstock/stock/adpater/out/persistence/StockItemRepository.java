package project.tosstock.stock.adpater.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.stock.adpater.out.entity.StockItemEntity;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, Long> {

	List<StockItemEntity> findByStockIdAndItemDateAfter(Long stockId, LocalDateTime time);
}
