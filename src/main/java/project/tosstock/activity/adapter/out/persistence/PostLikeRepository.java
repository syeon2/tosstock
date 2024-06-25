package project.tosstock.activity.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.activity.adapter.out.entity.PostLikeEntity;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

	void deleteByMemberIdAndPostId(Long memberId, Long postId);

	int countByPostId(Long postId);
}
