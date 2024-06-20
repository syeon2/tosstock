package project.tosstock.activity.adapter.out.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.activity.adapter.out.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findByPostId(Long postId, Pageable pageable);

	int countByPostId(Long postId);
}
