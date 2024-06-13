package project.tosstock.stock.adpater.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.application.domain.model.Market;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

	Page<StockEntity> findByNameContaining(String name, Pageable pageable);

	Page<StockEntity> findBySymbol(String symbol, Pageable pageable);

	Page<StockEntity> findByMarket(Market market, Pageable pageable);
}
