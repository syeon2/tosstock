package project.tosstock.activity.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.activity.adapter.out.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {

	Page<PostEntity> findByArticleContaining(String article, Pageable pageable);

	Page<PostEntity> findByStockId(Long stockId, Pageable pageable);
}
