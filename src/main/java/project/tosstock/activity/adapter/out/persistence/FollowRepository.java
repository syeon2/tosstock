package project.tosstock.activity.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.activity.adapter.out.entity.FollowEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowEntity.PK> {
}
