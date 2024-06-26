package project.tosstock.activity.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.activity.adapter.out.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {

}
