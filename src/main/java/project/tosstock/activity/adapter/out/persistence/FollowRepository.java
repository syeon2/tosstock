package project.tosstock.activity.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.tosstock.activity.adapter.out.entity.FollowEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

	@Modifying
	@Transactional
	@Query("delete from FollowEntity f where f.followerId = :followerId and f.followeeId = :followeeId")
	void deleteByFollowerIdAndFolloweeId(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);
}
